package com.zhenhui.demo.uac.core.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User extends Entity {

    private String phone;

    @JsonIgnore
    private String secret;

    private String nickname;

    private String avatar;

    private Profile profile = new Profile();

    private List<String> authorities = new ArrayList<>();
}







