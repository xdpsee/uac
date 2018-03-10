package com.zhenhui.demo.uac.common;

public enum ErrorCode {
    UNKNOWN(-3, "未知错误"),
    TOKEN_EXPIRED(-2, "令牌已过期"),
    TOKEN_INVALID(-1, "无效的令牌"),
    SUCCESS(0, "SUCCESS"),
    PHONE_NUMBER_INVALID(10001, "无效的电话号码"),
    PHONE_NUMBER_REGISTERED(10002, "电话号码已被注册"),


    CAPTCHA_SEND_FAILURE(10003, "发送验证码失败"),

    CAPTCHA_MISMATCH(10004, "验证码错误"),
    CAPTCHA_EXPIRED(10005, "验证码已过期"),

    SECRET_TOOL_SHORT(10006, "密码长度太短,8~16字符"),

    USER_NOT_FOUND(10007, "用户不存在"),
    SECRET_NOT_MATCH(10008, "密码错误");

    public final int code;
    public final String comment;

    ErrorCode(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }
}
