package com.onezol.vertex.common.model.record;

import lombok.Data;

@Data
public class OptionType {

    /**
     * 选项标签
     */
    private String label;

    /**
     * 选项值
     */
    private Integer value;
}
