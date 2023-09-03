package com.onezol.vertex.core.module.monitor.model.dto;


import com.onezol.vertex.core.module.monitor.model.record.*;
import lombok.Data;

import java.util.List;

@Data
public class SystemInfoWrapper {

    private Server server;

    private CPU cpu;

    private JVM jvm;

    private Memory memory;

    private List<FileSystem> fileSystems;

}
