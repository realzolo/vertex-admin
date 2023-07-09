package com.onezol.vertex.core.model.dto;


import com.onezol.vertex.core.model.pojo.system.*;

import java.util.List;

public class SystemInfoWrapper {
    private Server server;
    private CPU cpu;
    private JVM jvm;
    private Memory memory;
    private List<FileSystem> fileSystems;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public JVM getJvm() {
        return jvm;
    }

    public void setJvm(JVM jvm) {
        this.jvm = jvm;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public List<FileSystem> getFileSystems() {
        return fileSystems;
    }

    public void setFileSystems(List<FileSystem> fileSystems) {
        this.fileSystems = fileSystems;
    }
}
