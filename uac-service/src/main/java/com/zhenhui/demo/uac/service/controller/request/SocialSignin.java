package com.zhenhui.demo.uac.service.controller.request;

import com.zhenhui.demo.uac.common.SocialType;
import lombok.Data;

@Data
public class SocialSignin {

    private SocialType type;

    private Long uid;

    private String token;

}
