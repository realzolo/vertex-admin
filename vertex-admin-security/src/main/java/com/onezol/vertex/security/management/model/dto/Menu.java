package com.onezol.vertex.security.management.model.dto;

import com.onezol.vertex.common.model.dto.BaseDTO;

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

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Boolean getFrame() {
        return isFrame;
    }

    public void setFrame(Boolean frame) {
        isFrame = frame;
    }

    public Boolean getCache() {
        return isCache;
    }

    public void setCache(Boolean cache) {
        isCache = cache;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
