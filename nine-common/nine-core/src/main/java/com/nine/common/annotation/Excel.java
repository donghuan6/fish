package com.nine.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {

    /**
     * 列顺序
     */
    int order() default 0;

    /**
     * 列名
     */
    String name();

    /**
     * 列类型
     */
    ColumnType type() default ColumnType.STRING;


    public enum ColumnType {

        STRING,
        NUMBER,
        DATE,
        BOOLEAN,
        IMAGE;
    }
}
