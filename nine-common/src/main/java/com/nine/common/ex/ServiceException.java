package com.nine.common.ex;

import lombok.Getter;

/**
 * 业务错误
 *
 * @author fan
 */
@Getter
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "message='" + message + '\'' +
                ", cause=" + getCause() +
                '}';
    }
}
