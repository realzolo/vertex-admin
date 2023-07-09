package com.onezol.vertex.core.service.impl;

import com.onezol.vertex.common.util.MathUtils;
import com.onezol.vertex.common.util.NetUtils;
import com.onezol.vertex.common.util.StringUtils;
import com.onezol.vertex.core.model.dto.SystemInfoWrapper;
import com.onezol.vertex.core.model.pojo.system.*;
import com.onezol.vertex.core.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.lang.ref.WeakReference;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class MonitorServiceImpl implements MonitorService {
    private static WeakReference<SystemInfoWrapper> systemInfoWrapperCache = new WeakReference<>(null);
    private static LocalDateTime lastTime = LocalDateTime.now();
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取当前系统监控信息
     *
     * @return 当前系统监控信息
     */
    @Override
    public SystemInfoWrapper getSystemInfo() {
        // 从缓存中获取, 10秒内不重复获取
        if (systemInfoWrapperCache.get() != null && lastTime.plusSeconds(10).isAfter(LocalDateTime.now())) {
            return systemInfoWrapperCache.get();
        }

        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        CPU cpu = getCPUInfo(hardware.getProcessor());
        Memory memory = getMemoryInfo(hardware.getMemory());
        Server server = getServerInfo();
        JVM jvm = getJVMInfo();
        List<FileSystem> fileSystemInfo = getFileSystemInfo(systemInfo.getOperatingSystem());

        SystemInfoWrapper wrapper = new SystemInfoWrapper();
        wrapper.setCpu(cpu);
        wrapper.setMemory(memory);
        wrapper.setServer(server);
        wrapper.setJvm(jvm);
        wrapper.setFileSystems(fileSystemInfo);

        // 缓存
        systemInfoWrapperCache = new WeakReference<>(wrapper);
        lastTime = LocalDateTime.now();

        return wrapper;
    }

    /**
     * 获取缓存(Redis)信息
     *
     * @return 缓存信息
     */
    @Override
    public Object getCacheInfo() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        assert commandStats != null;
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return result;
    }

    /**
     * 获取CPU信息
     *
     * @return CPU信息
     */
    private CPU getCPUInfo(CentralProcessor processor) {
        CPU cpu = new CPU();

        long[] prevTicks = processor.getSystemCpuLoadTicks();
        int WAIT_SECOND = 1000;
        Util.sleep(WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        cpu.setCore(processor.getLogicalProcessorCount());
        cpu.setTotal(totalCpu);
        cpu.setSys(cSys);
        cpu.setUsed(user);
        cpu.setWait(iowait);
        cpu.setFree(idle);

        return cpu;
    }

    /**
     * 获取内存信息
     *
     * @return 内存信息
     */
    private Memory getMemoryInfo(GlobalMemory globalMemory) {
        Memory memory = new Memory();

        memory.setTotal(globalMemory.getTotal());
        memory.setUsed(globalMemory.getTotal() - globalMemory.getAvailable());
        memory.setFree(globalMemory.getAvailable());

        return memory;
    }

    /**
     * 获取服务器信息
     *
     * @return 系统信息
     */
    private Server getServerInfo() {
        Server server = new Server();

        Properties props = System.getProperties();
        server.setHostname(NetUtils.getHostName());
        server.setIp(NetUtils.getHostIp());
        server.setOsName(props.getProperty("os.name"));
        server.setOsArch(props.getProperty("os.arch"));
        server.setProjectPath(props.getProperty("user.dir"));

        return server;
    }

    /**
     * 获取JVM信息
     *
     * @return JVM信息
     */
    private JVM getJVMInfo() {
        JVM jvm = new JVM();

        Properties props = System.getProperties();
        jvm.setTotal(Runtime.getRuntime().totalMemory());
        jvm.setMax(Runtime.getRuntime().maxMemory());
        jvm.setFree(Runtime.getRuntime().freeMemory());
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));

        return jvm;
    }

    /**
     * 获取文件系统信息
     *
     * @return 磁盘信息
     */
    private List<FileSystem> getFileSystemInfo(OperatingSystem os) {
        List<FileSystem> fileSystems = new ArrayList<>();

        oshi.software.os.FileSystem fs = os.getFileSystem();
        List<OSFileStore> fileStores = fs.getFileStores();
        for (OSFileStore ofs : fileStores) {
            long free = ofs.getUsableSpace();
            long total = ofs.getTotalSpace();
            long used = total - free;
            FileSystem fileSystem = new FileSystem();
            fileSystem.setDrivePath(ofs.getMount());
            fileSystem.setDriveType(ofs.getType());
            fileSystem.setFileSystemType(ofs.getName());
            fileSystem.setTotal(convertFileSize(total));
            fileSystem.setFree(convertFileSize(free));
            fileSystem.setUsed(convertFileSize(used));
            fileSystem.setUsage(MathUtils.mul(MathUtils.div(used, total, 4), 100));
            fileSystems.add(fileSystem);
        }

        return fileSystems;
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}
