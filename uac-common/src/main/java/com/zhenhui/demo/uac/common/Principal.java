package com.zhenhui.demo.uac.common;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Principal implements Serializable {

    private static final long serialVersionUID = -123490806309753L;

    private long userId;

    private String phone;

    private SocialType type;

    private long openId;

    private List<String> authorities = new ArrayList<>();

}
