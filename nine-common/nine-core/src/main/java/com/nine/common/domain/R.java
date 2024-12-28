package com.nine.common.domain;

import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应体
 *
 * @param <T> data 类型
 * @author fan
 */
@Getter
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成功状态码 200
     */
    public static final int SUCCESS_CODE = HttpStatus.HTTP_OK;
    /**
     * 失败状态码 500
     */
    public static final int ERROR_CODE = HttpStatus.HTTP_INTERNAL_ERROR;

    private final int code;
    private final String msg;
    private final T data;

    public R(T data) {
        this(SUCCESS_CODE, data, null);
    }

    public R(T data, String msg) {
        this(SUCCESS_CODE, data, msg);
    }

    public R(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    @JsonIgnore
    public boolean isOk() {
        return this.code == SUCCESS_CODE;
    }

    @JsonIgnore
    public boolean isFail() {
        return this.code != SUCCESS_CODE;
    }

    public static <T> R<T> ok() {
        return new R<>(SUCCESS_CODE, null, null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(SUCCESS_CODE, data, null);
    }

    public static <T> R<T> ok(String msg) {
        return new R<>(SUCCESS_CODE, null, msg);
    }

    public static <T> R<T> ok(T data, String msg) {
        return new R<>(SUCCESS_CODE, data, msg);
    }

    public static <T> R<T> fail() {
        return new R<>(ERROR_CODE, null, null);
    }

    public static <T> R<T> fail(T data) {
        return new R<>(ERROR_CODE, data, null);
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(ERROR_CODE, null, msg);
    }

    public static <T> R<T> fail(T data, String msg) {
        return new R<>(ERROR_CODE, data, msg);
    }

    @Override
    public String toString() {
        return "R{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
