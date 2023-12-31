package com.onezol.vertex.security.api.model.dto;

import com.onezol.vertex.common.model.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseDTO {
    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    private String menuType;

    /**
     * 显示状态（true显示 false隐藏）
     */
    private Boolean visible;

    /**
     * 菜单状态（true正常 false停用）
     */
    private Boolean status;

    /**
     * 权限字符串
     */
    private String perms;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 父菜单名称
     */
    private String parentName;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 是否为外链（true是 false否）
     */
    private Boolean isFrame;

    /**
     * 是否缓存（true缓存 false不缓存）
     */
    private Boolean isCache;

    /**
     * 路由参数
     */
    private String query;

    /**
     * 备注
     */
    private String remark;
}
