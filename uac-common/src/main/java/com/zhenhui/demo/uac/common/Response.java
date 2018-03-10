package com.zhenhui.demo.uac.common;

import lombok.Data;

@Data
public final class Response<T> {

    private int error;

    private String message;

    private T data;


    public static <T> Response success(T data) {
        Response<T> response = new Response<>();
        response.error = 0;
        response.message = "ok";
        response.data = data;

        return response;
    }

    public static <T> Response success(T data, String message) {
        Response<T> response = new Response<>();
        response.error = 0;
        response.message = message;
        response.data = data;

        return response;
    }

    public static <T> Response error(int error, String message) {
        Response<T> response = new Response<>();
        response.error = error;
        response.message = message;
        response.data = null;

        return response;
    }

    public static <T> Response error(ErrorCode error) {
        Response<T> response = new Response<>();
        response.error = error.code;
        response.message = error.comment;
        response.data = null;

        return response;
    }

}
