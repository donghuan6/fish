package com.nine.common.context;

import cn.hutool.core.convert.Convert;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.nine.common.constans.UserToken;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程上下文
 */
public class ContextHolder {

    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL
            = new TransmittableThreadLocal<>();

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (Objects.isNull(map)) {
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, Object> map) {
        THREAD_LOCAL.set(map);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static String get(String key) {
        return Convert.toStr(getLocalMap().get(key), null);
    }

    public static void set(String key, Object value) {
        Object v = Objects.isNull(value) ? "" : value;
        getLocalMap().put(key, v);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return Convert.convert(clazz, getLocalMap().get(key));
    }

    public static Long getUserId() {
        return Convert.toLong(get(UserToken.USER_ID), null);
    }

    public static String getUsername() {
        return Convert.toStr(get(UserToken.USERNAME), null);
    }

    public static String getUserKey() {
        return Convert.toStr(get(UserToken.USER_KEY), null);
    }

    public static void setUserId(Long userId) {
        set(UserToken.USER_ID, userId);
    }

    public static void setUsername(String username) {
        set(UserToken.USERNAME, username);
    }

    public static void setUserKey(String userKey) {
        set(UserToken.USER_KEY, userKey);
    }


}
