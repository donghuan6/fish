package com.nine.common.enums;

/**
 * 用户状态：0-正常，1-禁用
 */
public enum UserStatusEnum {

    normal(0,"正常"),
    disable(1,"禁用");

    final int code;
    final String desc;

    UserStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
