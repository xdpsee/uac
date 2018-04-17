package com.zhenhui.demo.uac.service.controller.response;

import com.zhenhui.demo.uac.common.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private Long userId;

    private String name;

    private String avatar;

    private Gender gender;

    private String birthday;

    private String country;

    private String province;

    private String city;

    private String address;


}
