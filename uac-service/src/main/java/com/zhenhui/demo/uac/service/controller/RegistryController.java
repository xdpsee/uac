package com.zhenhui.demo.uac.service.controller;

import com.zhenhui.demo.uac.common.Response;
import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.core.repository.UserRepository;
import com.zhenhui.demo.uac.core.repository.exception.UserAlreadyExistsException;
import com.zhenhui.demo.uac.common.ErrorCode;
import com.zhenhui.demo.uac.service.manager.CaptchaManager;
import com.zhenhui.demo.uac.service.utils.PhoneUtils;
import com.zhenhui.demo.uac.service.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/user/registry")
public class RegistryController {

    private static final Logger logger = LoggerFactory.getLogger(RegistryController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CaptchaManager captchaManager;
    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping(path = "", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Response<String> registry(@RequestParam("phone") String phone
            , @RequestParam("secret") String secret
            , @RequestParam("captcha") String rawCaptcha) {

        if (!PhoneUtils.isValid(phone)) {
            return Response.error(ErrorCode.PHONE_NUMBER_INVALID);
        }

        try {
            final String captcha = captchaManager.lookupCaptcha(phone);
            if (StringUtils.isEmpty(captcha)) {
                return Response.error(ErrorCode.CAPTCHA_EXPIRED);
            }

            if (!captcha.equals(rawCaptcha)) {
                return Response.error(ErrorCode.CAPTCHA_MISMATCH);
            }

            if (StringUtils.isEmpty(secret) || secret.length() < 8) {
                return Response.error(ErrorCode.SECRET_TOOL_SHORT);
            }

            final User user = userRepository.createUser(phone, secret);
            captchaManager.invalidCaptcha(phone);

            return Response.success(tokenUtils.createToken(user));

        } catch (UserAlreadyExistsException e) {
            return Response.error(ErrorCode.PHONE_NUMBER_REGISTERED);
        } catch (Exception e) {
            logger.error("RegistryController.registry exception = {}", e);
            return Response.error(ErrorCode.UNKNOWN);
        }
    }

}
