package com.onezol.platform.constant;

/**
 * 常量类
 * <p/>
 * RK: Redis Key <br/>
 * P: Prefix <br/>
 */
public interface Constant {
    // 登录状态
    String LOGIN_STATE = "loginState";

    // http协议
    String P_PROTOCOL_HTTP = "http://";

    // https协议
    String P_PROTOCOL_HTTPS = "https://";

    // Authorization header 前缀
    String P_AUTHORIZATION_HEADER = "Bearer ";

    // Redis key: 用户信息前缀
    String P_RK_USER = "user:";

    // NULL值标识符
    String S_CONTROLLER_NULL_RESP = "::SYMBOL::4CCA07368BEB40DA0DB2101421C9E46B::";

    // Redis key: 字典
    String RK_DICTIONARY = "dictionary";

    // Redis key: 枚举选项
    String RK_ENUM_OPTIONS = "enumOptions";

    // 最大页大小
    int MAX_PAGE_SIZE = 500;

    // Redis key: 邮箱验证码前缀
    String P_RK_EMAIL_CODE = "emailCode:";

    // unknown
    String UNKNOWN = "unknown";
}
