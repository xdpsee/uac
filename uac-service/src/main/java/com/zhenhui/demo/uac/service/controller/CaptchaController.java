package com.zhenhui.demo.uac.service.controller;

import com.zhenhui.demo.uac.common.Response;
import com.zhenhui.demo.uac.service.common.ErrorCodes;
import com.zhenhui.demo.uac.service.manager.CaptchaManager;
import com.zhenhui.demo.uac.service.utils.PhoneUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaController.class);

    @Autowired
    private CaptchaManager captchaManager;

    @ResponseBody
    @RequestMapping("/registry")
    public Response<Boolean> registryCaptcha(@RequestParam("phone") String phone) {

        if (!PhoneUtils.isValid(phone)) {
            return Response.error(ErrorCodes.PHONE_NUMBER_INVALID);
        }

        String captcha = captchaManager.createRegistryCaptcha(phone, true);
        logger.info("create captcha: {}", captcha);

        try {
            Thread.sleep(1000);


        } catch (Exception e) {
            logger.error("send captcha to phone:{} failure.", phone);
            return Response.error(ErrorCodes.CAPTCHA_SEND_FAILURE);
        }

        return Response.success(true, captcha);
    }

}
