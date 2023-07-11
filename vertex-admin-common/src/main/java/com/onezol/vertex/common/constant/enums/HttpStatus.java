package com.onezol.vertex.common.constant.enums;


public enum HttpStatus implements EnumService {
    /**
     * 操作或请求成功<br>
     * 当操作或请求成功完成时使用
     */
    SUCCESS("操作成功", 10000),
    /**
     * 操作或请求失败<br>
     * 当操作或请求无法成功完成时使用
     */
    FAILURE("操作失败", 10001),
    /**
     * 登录失败<br>
     * 在用户进行登录操作时，如果提供的凭据无效或与数据库中存储的凭据不匹配，则可以使用此异常来指示登录失败。
     */
    LOGIN_FAILURE("登录失败", 10002),
    /**
     * 用户没有访问或执行特定操作的权限<br>
     * 当用户尝试访问受限资源或执行需要特定权限的操作时，可以使用此异常来指示其没有必要的权限。
     */
    NO_PERMISSION("无权限访问", 10003),
    /**
     * 禁止访问请求的资源或操作<br>
     * 当用户尝试访问被明确禁止或限制的资源或执行受限制的操作时，可以使用此异常来指示其无法进行访问。
     */
    FORBIDDEN("禁止访问", 10004),
    /**
     * 用户未经授权或身份验证，无法执行请求的操作。<br>
     * 当用户尝试执行需要身份验证或授权的操作时，但未提供有效凭据或未通过身份验证时，可以使用此异常来指示其操作未经授权。
     */
    UNAUTHORIZED("未授权操作", 10005),
    /**
     * 请求参数错误或输入无效<br>
     * 当请求中包含无效参数、缺少必需的参数或参数格式不正确时，可以使用此异常来指示参数错误。
     */
    PARAM_ERROR("请求参数错误", 10006);


    private final String value;
    private final int code;

    HttpStatus(String value, int code) {
        this.value = value;
        this.code = code;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public int getCode() {
        return code;
    }

}
