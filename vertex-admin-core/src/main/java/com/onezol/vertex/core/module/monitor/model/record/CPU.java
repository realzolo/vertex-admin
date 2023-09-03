package com.onezol.vertex.core.module.monitor.model.record;


import lombok.Data;

/**
 * CPU相关信息
 *
 */
@Data
public class CPU {
    /**
     * 核心数
     */
    private int core;

    /**
     * CPU总的使用率
     */
    private double total;

    /**
     * CPU系统使用率
     */
    private double sys;

    /**
     * CPU用户使用率
     */
    private double used;

    /**
     * CPU当前等待率
     */
    private double wait;

    /**
     * CPU当前空闲率
     */
    private double free;
}
