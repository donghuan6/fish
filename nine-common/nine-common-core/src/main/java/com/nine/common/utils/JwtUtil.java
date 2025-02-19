package com.nine.common.utils;


import cn.hutool.core.convert.Convert;
import cn.hutool.jwt.JWTUtil;
import com.nine.common.constans.UserToken;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JwtUtil {

    /**
     * jwt 验证签名 token
     */
    public static boolean verifyToken(String token) {
        return JWTUtil.verify(token, UserToken.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * jwt 创建 token
     */
    public static String createToken(Map<String, Object> payloads) {
        return JWTUtil.createToken(payloads, UserToken.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * jwt 解析 token
     */
    public static Map<String, Object> parseToken(String token) {
        return JWTUtil.parseToken(token).getPayloads();
    }

    /**
     * 获取用户 userKey
     */
    public static String getUserKey(String token) {
        return Convert.toStr(parseToken(token).get(UserToken.USER_KEY));
    }

}
