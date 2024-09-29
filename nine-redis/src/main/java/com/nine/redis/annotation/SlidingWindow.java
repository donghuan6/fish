package com.nine.redis.annotation;

import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 滑动窗口
 *
 * @author fan
 */
@Order(200)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlidingWindow {


    /**
     * 最大请求数量
     */
    int maxCount() default 5;

    /**
     * 窗口大小（秒）
     */
    int windowSize() default 10;

    /**
     * key EL
     */
    String keyEL();


}
