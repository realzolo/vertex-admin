package com.onezol.vertex.common.constant;

/**
 * Redis常量
 */
public interface RedisKey {
    /**
     * 用户信息前缀
     */
    String ONLINE_USERID_SET = "online_userid_set";
    String ONLINE_USER = "online_userinfo:";

    /**
     * 字典
     */
    String DICTIONARY = "dictionary";

    /**
     * 枚举
     */
    String ENUM = "enum";

}
