package com.nine.security.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内部认证
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InnerAuth {

    /**
     * 是否需要验证用户信息
     * 校验请求头中的 authorization 字段
     * 校验请求头中的 user_id、username
     * 校验当前线程中的 userVo 信息
     */
    boolean checkHasUser() default false;


}
