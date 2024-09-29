package com.nine.redis.annotation;

import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法级别的分布式锁注解
 * 不具备公平，锁续时
 *
 * @author fan
 */
@Order(100)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    /**
     * 如果不指定，则以方法名和参数作为 key
     */
    String keyEL() default "";

    /**
     * 锁的超时时间
     */
    int timeOutSeconds() default 30;

}
