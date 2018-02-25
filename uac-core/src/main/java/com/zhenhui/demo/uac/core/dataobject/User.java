package com.zhenhui.demo.uac.core.dataobject;

import lombok.Data;

@Data
public class User extends Entity {

    private String phone;

    private String secret;

    private String nickname;

    private String avatar;

    private Profile profile = new Profile();
}






