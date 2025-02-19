package com.nine.common.constans;

public interface Redis {

    /**
     * 钉钉 token 前缀
     */
    String DINGTALK_TOKEN_PREFIX = "dingtalk:token:";

    /**
     * 登录用户 token
     */
    String USER_LOGIN_TOKEN_PREFIX = "user:login:token:";

    /**
     * 用户登录失败前缀
     */
    String USER_LOGIN_FAIL_PREFIX = "user:login:fail:";


}
