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
    String JWT_SECRET = "nine";

    /**
     * 用户 key, redis key 对应用户信息
     */
    String USER_KEY = "user_key";

    /**
     * 用户信息
     */
    String LOGIN_USER = "login_user";

    /**
     * 用户 id
     */
    String USER_ID = "user_id";

    /**
     * 用户名
     */
    String USERNAME = "username";

    /**
     * 昵称
     */
    String NICK_NAME = "nick_name";

    /**
     * 访问 token，请求头中：认证 Authorization 值
     */
    String ACCESS_TOKEN = "access_token";

    /**
     * 用户 token 过期 key
     */
    String EXPIRE_MINUTE = "expire_minute";

    /**
     * 缓存有效时长，分钟
     */
    int EXPIRE_MINUTE_TIME = 30;


}
