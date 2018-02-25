package com.zhenhui.demo.uac.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthInfo implements Serializable {

    private static final long serialVersionUID = -123490806309753L;

    private long userId;

    private String phone;

    private SocialType type;

    private long openId;

    private Boolean isAdmin = false;

}
