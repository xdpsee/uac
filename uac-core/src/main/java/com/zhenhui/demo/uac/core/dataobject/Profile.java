package com.zhenhui.demo.uac.core.dataobject;

import java.io.Serializable;

import com.zhenhui.demo.uac.common.Gender;
import lombok.Data;

@Data
public class Profile implements Serializable {

    private static final long serialVersionUID = -12980864316641L;

    private Gender gender;

    private String birthday;

    private String country;

    private String province;

    private String city;

    private String address;
}




