package com.zhenhui.demo.uac.security.exception;

public class InvalidTokenException extends Exception {

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
