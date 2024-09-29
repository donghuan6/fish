package com.nine.redis.annotation;

import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时间窗口
 *
 * @author fan
 */
@Order(300)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeWindow {

    /**
     * 每分钟 5 个
     */
    int maxPreMinute() default 5;

    /**
     * 每小时 20 个
     */
    int maxPreHour() default 20;

    /**
     * 每天 50 个
     */
    int maxPreDay() default 50;

    /**
     * key EL
     */
    String keyEL();

}
