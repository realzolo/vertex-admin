package com.onezol.app.config;

import com.onezol.app.util.JsonUtils;
import com.onezol.app.util.NetUtils;
import com.onezol.app.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.onezol.app.constant.Constant.PROTOCOL_HTTP;

@Configuration
@ConditionalOnProperty(prefix = "frp", name = "enable", havingValue = "true")
public class FrpConfig implements ApplicationListener<AvailabilityChangeEvent<ReadinessState>>, InitializingBean {
    public static final String FRP_DIR = "frp/"; // frp目录
    public static final String FRP_CONFIG_FILE = FRP_DIR + "frpc.ini"; // frp配置文件
    private static final Logger logger = LoggerFactory.getLogger(FrpConfig.class);
    private static int MAX_TEST_TIMES = 3; // frp服务连接测试最大重试次数
    private static Process process; // frp客户端进程
    private Map<String, Map<String, String>> FRP_CONFIG = new HashMap<>(); // frp服务配置
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取操作系统对应客户端
     *
     * @return 客户端路径
     */
    private static String getClientPath() {
        String osName = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        // windows
        if (osName.startsWith("Windows") && arch.contains("64")) {
            return FRP_DIR + "frpc_windows_amd64.exe";
        }
        if (osName.startsWith("Windows") && arch.contains("86")) {
            return FRP_DIR + "frpc_windows_386.exe";
        }
        // mac
        if (osName.startsWith("Mac")) {
            return FRP_DIR + "frpc_darwin";
        }
        // linux
        if (osName.startsWith("Linux") && arch.contains("64")) {
            return FRP_DIR + "frpc_linux_amd64";
        }
        if (osName.startsWith("Linux") && arch.contains("86")) {
            return FRP_DIR + "frpc_linux_386";
        }
        if (osName.startsWith("Linux") && arch.contains("arm")) {
            return FRP_DIR + "frpc_linux_arm64";
        }
        return "";
    }

    /**
     * 启动frp
     *
     * @throws Exception 解析frp配置文件
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 1. 解析文件
        parseIni();

        // 2. 启动frp服务
        startup();
    }

    /**
     * 应用启动完成后测试frp服务是否可用
     *
     * @param event 事件
     */
    @Override
    public void onApplicationEvent(AvailabilityChangeEvent event) {
        if (event.getState() == ReadinessState.ACCEPTING_TRAFFIC) {
            testNetwork(FRP_CONFIG);
        }
    }

    private void startup() {
        logger.info("正在启动frp服务...");

        ClassPathResource frpIni = new ClassPathResource(FRP_CONFIG_FILE);
        ClassPathResource frpExe = new ClassPathResource(getClientPath());
        try {
            String frpInitPath = frpIni.getFile().getAbsolutePath();
            String frpExePath = frpExe.getFile().getAbsolutePath();
            process = Runtime.getRuntime().exec(frpExePath + " -c " + frpInitPath);
            logger.info("frp服务已启动!");
        } catch (Exception e) {
            throw new RuntimeException("frp服务启动失败! 请检查配置文件或远程服务器是否可用!");
        }
    }

    private void testNetwork(Map<String, Map<String, String>> configMap) {
        logger.info("测试frp服务是否可用...");
        final int maxTestTimes = MAX_TEST_TIMES;
        String serverAddr = getMapValue(configMap, "common.server_addr");
        String remotePort = getMapValue(configMap, "server.remote_port");
        serverAddr = this.getIpAddrByDomain(serverAddr);
        String testUrl = String.format("%s%s:%s%s/network/ping", PROTOCOL_HTTP, serverAddr, remotePort, contextPath);
        boolean success = false;
        while (MAX_TEST_TIMES-- > 0) {
            try {
                String result = restTemplate.getForObject(testUrl, String.class);
                Map<String, Object> ajaxResult = JsonUtils.fromJson(result, Map.class);
                if (Objects.equals("pong", ajaxResult.get("data"))) {
                    success = true;
                    logger.info("frp服务状态：(ACTIVE)可用");
                    printFrpInfo(configMap);
                    return;
                }
            } catch (Exception e) {
                logger.info("frp服务连接超时，第{}次重试...", maxTestTimes - MAX_TEST_TIMES);
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if (!success) {
            logger.warn("frp服务状态：(DOWN)不可用");
        }
    }

    private void printFrpInfo(Map<String, Map<String, String>> configMap) {
        String remoteAddr = getMapValue(configMap, "common.server_addr");
        remoteAddr = this.getIpAddrByDomain(remoteAddr);
        String serverRemotePort = getMapValue(configMap, "server.remote_port");
        String webRemotePort = getMapValue(configMap, "web.remote_port");
        logger.info("访问地址：{}{}:{}{}", PROTOCOL_HTTP, remoteAddr, serverRemotePort, contextPath);
        if (StringUtils.hasText(webRemotePort)) {
            logger.info("web访问地址：{}{}:{}", PROTOCOL_HTTP, remoteAddr, webRemotePort);
        }
    }

    /**
     * 解析frp.ini文件
     *
     * @throws IOException 文件读取异常
     */
    private void parseIni() throws IOException {
        logger.info("正在解析frp.ini文件...");
        Map<String, Map<String, String>> configMap = new HashMap<>();
        ClassPathResource frpIni = new ClassPathResource(FRP_CONFIG_FILE);
        InputStream inputStream = frpIni.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        Map<String, String> currentMap = null;
        while ((line = bufferedReader.readLine()) != null) {
            line = line.trim();
            // 读取到[xxx]的行，表示进入了一个新的配置块
            if (line.startsWith("[")) {
                String key = line.substring(1, line.length() - 1);
                currentMap = new HashMap<>();
                configMap.put(key, currentMap);
                continue;
            }
            // 读取到#或者;或者空行，表示注释或者空行，跳过
            if (line.startsWith("#") || line.startsWith(";") || line.isEmpty()) {
                continue;
            }
            // 读取到key=value的行，表示配置项
            String[] split = line.split("=");
            assert currentMap != null;
            currentMap.put(split[0].trim(), split[1].trim());
        }
        FRP_CONFIG = configMap;
        logger.info("frp.ini文件解析成功!");
    }

    private String getIpAddrByDomain(String domain) {
        if (!NetUtils.isIpv4(domain)) {
            return NetUtils.getIpAddrByDomain(domain);
        }
        return domain;
    }

    private String getMapValue(Map<String, Map<String, String>> map, String key) {
        if (map == null) {
            throw new IllegalArgumentException("map is null");
        }
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        String[] vars = key.split("\\.");

        Map<String, String> subMap = map.get(vars[0]);
        if (subMap == null) {
            return null;
        }
        return subMap.get(vars[1]);
    }

    @Component
    @ConditionalOnProperty(prefix = "frp", name = "enable", havingValue = "true")
    public static class ApplicationClosedEventListener implements ApplicationListener<ContextClosedEvent> {
        @Override
        public void onApplicationEvent(ContextClosedEvent event) {
            process.destroy();
            logger.info("frp服务已关闭!");
        }
    }
}
