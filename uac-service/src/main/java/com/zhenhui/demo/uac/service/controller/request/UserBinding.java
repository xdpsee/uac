package com.zhenhui.demo.uac.service.controller.request;

import com.zhenhui.demo.uac.common.SocialType;
import lombok.Data;

@Data
public class UserBinding {

    private String phone;

    private String secret;

    private SocialType type;

    private Long openId;

    private String captcha;

}
