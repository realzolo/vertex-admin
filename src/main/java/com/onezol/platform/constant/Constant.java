package com.onezol.platform.constant;

/**
 * 常量类
 * <p/>
 * RK: Redis Key <br/>
 * P: Prefix <br/>
 */
public class Constant {
    // 登录状态
    public static final String LOGIN_STATE = "loginState";

    // http协议
    public static final String P_PROTOCOL_HTTP = "http://";

    // https协议
    public static final String P_PROTOCOL_HTTPS = "https://";

    // Authorization header 前缀
    public static final String P_AUTHORIZATION_HEADER = "Bearer ";

    // Redis key: 用户信息前缀
    public static final String P_RK_USER = "user:";

    // NULL值标识符
    public static final String S_CONTROLLER_NULL_RESP = "::SYMBOL::4CCA07368BEB40DA0DB2101421C9E46B::";
}
