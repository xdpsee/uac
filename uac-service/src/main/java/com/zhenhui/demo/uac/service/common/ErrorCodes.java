package com.zhenhui.demo.uac.service.common;

import com.zhenhui.demo.uac.common.ErrorCode;

public enum ErrorCodes implements ErrorCode {
    UNKNOWN(-2, "未知错误"),
    TOKEN_EXPIRED(-1, "用户认证已过期,请重新登录"),
    SUCCESS(0, "SUCCESS"),
    PHONE_NUMBER_INVALID(10001, "无效的电话号码"),
    PHONE_NUMBER_REGISTERED(10002, "电话号码已被注册"),


    CAPTCHA_SEND_FAILURE(10003, "发送验证码失败"),

    CAPTCHA_MISMATCH(10004, "验证码错误"),
    CAPTCHA_EXPIRED(10005, "验证码已过期"),

    SECRET_TOOL_SHORT(10006, "密码长度太短,8~16字符")
    ;

    public final int code;
    public final String comment;
    ErrorCodes(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String comment() {
        return comment;
    }
}
