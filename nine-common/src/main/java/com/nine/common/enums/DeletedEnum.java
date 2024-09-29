package com.nine.common.enums;

/**
 * 0-存在
 * 1-删除
 */
public enum DeletedEnum {

    EXISTS(0, "存在"),
    DELETED(1, "删除");

    final int code;
    final String desc;

    DeletedEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
