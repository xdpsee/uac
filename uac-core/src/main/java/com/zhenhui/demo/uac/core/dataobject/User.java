package com.zhenhui.demo.uac.core.dataobject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User extends Entity {

    private String phone;

    private String secret;

    private String nickname;

    private String avatar;

    private Profile profile = new Profile();

    private List<String> authorities = new ArrayList<>();
}







