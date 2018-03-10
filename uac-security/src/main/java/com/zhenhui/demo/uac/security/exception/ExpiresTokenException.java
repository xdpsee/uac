package com.zhenhui.demo.uac.security.exception;

public class ExpiresTokenException extends Exception {

    public ExpiresTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}

