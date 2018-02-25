package com.zhenhui.demo.uac.core.dataobject;

import com.zhenhui.demo.uac.common.SocialType;
import lombok.Data;

@Data
public class SocialAccount extends Entity {

    private SocialType type;

    private Long openId;

    private String token;

    private String nickname;

    private String avatar;

    private Boolean activated;

    private Long userId = 0L;
}


