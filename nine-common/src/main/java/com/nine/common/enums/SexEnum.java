package com.nine.common.enums;

/**
 * 性别
 */
public enum SexEnum {

    UNKNOW(0, "未知"),
    MAN(1, "男"),
    WOMAN(2, "女");

    final int code;
    final String desc;

    SexEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
