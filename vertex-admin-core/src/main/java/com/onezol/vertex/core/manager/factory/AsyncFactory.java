package com.onezol.vertex.core.manager.factory;

import com.onezol.vertex.common.constant.enums.LoginStatus;
import com.onezol.vertex.common.util.NetUtils;
import com.onezol.vertex.common.util.RequestUtils;
import com.onezol.vertex.common.util.SpringUtils;
import com.onezol.vertex.core.model.dto.UserLoginLog;
import com.onezol.vertex.core.service.UserLoginLogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 */
public class AsyncFactory {
    private static final Logger logger = LoggerFactory.getLogger(AsyncFactory.class);

    /**
     * 记录登录日志
     *
     * @param username 用户名
     * @param status   状态
     * @param args     列表
     * @return 任务task
     */
    public static TimerTask recordLoginLog(final String username, final LoginStatus status, final Object... args) {
        HttpServletRequest request = RequestUtils.getRequest();
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        final String ip = NetUtils.getIpAddr(request);
        final String loginStatus = status == LoginStatus.SUCCESS ? "登录成功" : "登录失败";
        return new TimerTask() {
            @Override
            public void run() {
                String address = NetUtils.getRealAddressByIP(ip);
                String s = String.format("%s -> %s(%s) %s(%s)", username, ip, address, loginStatus, status.getValue());

                logger.info(s, args);

                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();

                // 封装对象
                UserLoginLog userLoginLog = new UserLoginLog();
                userLoginLog.setUsername(username);
                userLoginLog.setStatus(loginStatus);
                userLoginLog.setIp(ip);
                userLoginLog.setLocation(address);
                userLoginLog.setBrowser(browser);
                userLoginLog.setOs(os);
                userLoginLog.setMessage(status.getValue());
                userLoginLog.setTime(LocalDateTime.now());

                // 插入数据
                SpringUtils.getBean(UserLoginLogService.class).createLog(userLoginLog);
            }
        };
    }

}
