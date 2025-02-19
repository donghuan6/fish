package com.nine.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysLog {

    /**
     * 服务名
     */
    String service();

    /**
     * 标题
     */
    String title();

    /**
     * 是否保存请求参数
     */
    boolean saveRequest() default true;

    /**
     * 是否保响应
     */
    boolean saveResponse() default true;

    /**
     * 指定排除记录的请求参数
     */
    String[] exclude() default {};


}
