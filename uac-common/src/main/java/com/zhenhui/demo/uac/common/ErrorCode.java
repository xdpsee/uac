package com.zhenhui.demo.uac.common;

public enum ErrorCode {
    SYSTEM_OVERLOAD(-4, "系统繁忙"),
    UNKNOWN(-3, "未知错误"),
    TOKEN_EXPIRED(-2, "令牌已过期"),
    TOKEN_INVALID(-1, "无效的令牌"),
    SUCCESS(0, "SUCCESS"),
    ACCESS_DENIED(10000, "禁止访问"),
    PHONE_NUMBER_INVALID(10001, "无效的电话号码"),
    PHONE_NUMBER_REGISTERED(10002, "电话号码已被注册"),


    CAPTCHA_SEND_FAILURE(10003, "发送验证码失败"),

    CAPTCHA_MISMATCH(10004, "验证码错误"),
    CAPTCHA_EXPIRED(10005, "验证码已过期"),

    SECRET_TOOL_SHORT(10006, "密码长度太短,8~16字符"),

    USER_NOT_FOUND(10007, "用户不存在"),
    SECRET_NOT_MATCH(10008, "密码错误"),
    WEIBO_GET_USER_ERROR(10009, "获取微博用户信息错误"),
    NO_USER_BOUND(10010, "社交账号未绑定系统账号"),
    WEIBO_NO_AUTH(10011, "微博未认证,请使用微博登陆重试"),
    USER_BIND_ERROR(10012, "用户绑定错误"),
    ;

    public final int code;
    public final String comment;

    ErrorCode(int code, String comment) {
        this.code = code;
        this.comment = comment;
    }
}
