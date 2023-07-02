package com.onezol.platform.manager;

import com.onezol.platform.model.pojo.system.System;
import com.onezol.platform.model.pojo.system.*;
import com.onezol.platform.util.MathUtils;
import com.onezol.platform.util.NetUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class SystemInformation {
    private static final int WAIT_SECOND = 1000;

    /**
     * CPU相关信息
     */
    private CPU cpu = new CPU();

    /**
     * 內存相关信息
     */
    private Memory memory = new Memory();

    /**
     * JVM相关信息
     */
    private JVM jvm = new JVM();

    /**
     * 系统相关信息
     */
    private System system = new System();

    /**
     * 文件系统相关信息
     */
    private List<FileSystem> fileSystems = new ArrayList<>();


    /**
     * 获取当前系统信息
     *
     * @return 当前系统信息
     */
    public static SystemInformation currentSystemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        CPU cpu = getCPUInfo(hardware.getProcessor());
        Memory memory = getMemoryInfo(hardware.getMemory());
        System system = getSystemInfo();
        JVM jvm = getJVMInfo();
        List<FileSystem> fileSystemInfo = getFileSystemInfo(systemInfo.getOperatingSystem());

        SystemInformation systemInformation = new SystemInformation();
        systemInformation.setCpu(cpu);
        systemInformation.setMemory(memory);
        systemInformation.setSystem(system);
        systemInformation.setJvm(jvm);
        systemInformation.setFileSystems(fileSystemInfo);

        return systemInformation;
    }


    /**
     * 获取CPU信息
     *
     * @return CPU信息
     */
    private static CPU getCPUInfo(CentralProcessor processor) {
        CPU cpu = new CPU();

        long[] prevTicks = processor.getSystemCpuLoadTicks();
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
    private static Memory getMemoryInfo(GlobalMemory globalMemory) {
        Memory memory = new Memory();

        memory.setTotal(globalMemory.getTotal());
        memory.setUsed(globalMemory.getTotal() - globalMemory.getAvailable());
        memory.setFree(globalMemory.getAvailable());

        return memory;
    }

    /**
     * 获取系统信息
     *
     * @return 系统信息
     */
    private static System getSystemInfo() {
        System system = new System();

        Properties props = java.lang.System.getProperties();
        system.setHostname(NetUtils.getHostName());
        system.setIp(NetUtils.getHostIp());
        system.setOsName(props.getProperty("os.name"));
        system.setOsArch(props.getProperty("os.arch"));
        system.setProjectPath(props.getProperty("user.dir"));

        return system;
    }

    /**
     * 获取JVM信息
     *
     * @return JVM信息
     */
    private static JVM getJVMInfo() {
        JVM jvm = new JVM();

        Properties props = java.lang.System.getProperties();
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
    private static List<FileSystem> getFileSystemInfo(OperatingSystem os) {
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
    public static String convertFileSize(long size) {
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

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public JVM getJvm() {
        return jvm;
    }

    public void setJvm(JVM jvm) {
        this.jvm = jvm;
    }

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public List<FileSystem> getFileSystems() {
        return fileSystems;
    }

    public void setFileSystems(List<FileSystem> fileSystems) {
        this.fileSystems = fileSystems;
    }
}
