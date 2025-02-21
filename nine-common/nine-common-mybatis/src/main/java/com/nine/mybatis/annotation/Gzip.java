package com.nine.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 针对操作 mybatis-plus crud 时，针对某些大字段或有乱码情况的，进行压缩与解压缩处理
 *
 * @author fan
 * @link com.nine.dao.config.MybatisGzipFiledInterceptor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Gzip {

}
