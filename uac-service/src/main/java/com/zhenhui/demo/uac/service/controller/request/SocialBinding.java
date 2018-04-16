package com.zhenhui.demo.uac.service.controller.request;

import com.zhenhui.demo.uac.common.SocialType;
import lombok.Data;

@Data
public class SocialBinding {

    private SocialType type;

    private Long openId;

    private String nickname;

    private String avatar;

}

