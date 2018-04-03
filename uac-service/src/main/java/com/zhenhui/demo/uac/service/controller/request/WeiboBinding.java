package com.zhenhui.demo.uac.service.controller.request;

import lombok.Data;

@Data
public class WeiboBinding {

    private String phone;

    private String captcha;

    private Long openId;

}

