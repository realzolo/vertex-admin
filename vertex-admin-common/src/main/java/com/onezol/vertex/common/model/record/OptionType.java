package com.onezol.vertex.common.model.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
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
