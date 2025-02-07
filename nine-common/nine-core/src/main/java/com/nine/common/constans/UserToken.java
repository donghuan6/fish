package com.nine.common.constans;

/**
 * 用户 token 相关常量
 */
public interface UserToken {

    /**
     * AUTHORIZATION 请求头前缀
     */
    String AUTHORIZATION_PREFIX = "Bearer ";

    /**
     * jwt 密钥
     */
    String JWT_SECRET = "xia_fan_jiujiu_baobao";

    /**
     * 用户 id
     */
    String USER_ID = "user_id";

    /**
     * 用户名
     */
    String USERNAME = "username";

    /**
     * 用户 token
     */
    String USER_TOKEN = "user_token";

    /**
     * 用户 token 过期 key
     */
    String EXPIRE_MINUTE = "expire_minute";

    /**
     * 缓存有效时长，分钟
     */
    int EXPIRE_MINUTE_TIME = 30;


}
