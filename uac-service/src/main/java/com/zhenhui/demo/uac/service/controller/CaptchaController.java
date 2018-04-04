package com.zhenhui.demo.uac.service.controller;

import com.zhenhui.common.SmsSendResult;
import com.zhenhui.common.SmsService;
import com.zhenhui.demo.uac.common.ErrorCode;
import com.zhenhui.demo.uac.common.JSONUtil;
import com.zhenhui.demo.uac.common.Response;
import com.zhenhui.demo.uac.service.manager.CaptchaManager;
import com.zhenhui.demo.uac.service.utils.PhoneUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@SuppressWarnings("unchecked")
@RestController
public class CaptchaController {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaController.class);

    @Autowired
    private CaptchaManager captchaManager;

    @Autowired
    private SmsService smsService;

    @Autowired
    private ExecutorService executorService;

    private static Map<String, Long> categoryTemplateMap = new HashMap<>();
    static {
        categoryTemplateMap.put("registry", 69463L);
    }

    @ResponseBody
    @RequestMapping(value = "/captcha", method = RequestMethod.POST)
    public DeferredResult<Response<Boolean>> getCaptcha(@RequestParam("phone") String phone, @RequestParam("category") String category) {

        final DeferredResult<Response<Boolean>> result = new DeferredResult<>();
        if (!PhoneUtils.isValid(phone)) {
            result.setResult(Response.error(ErrorCode.PHONE_NUMBER_INVALID));
            return result;
        }

        if (!categoryTemplateMap.containsKey(category)) {
            result.setResult(Response.error(ErrorCode.CAPTCHA_CATEGORY_INVALID));
            return result;
        }

        final String captcha = captchaManager.createCaptcha(phone, true);
        try {
            executorService.submit(() -> {
                Map<String, String> params = new HashMap<>();
                params.put("code", captcha);

                SmsSendResult sendResult = null;
                try {
                    sendResult = smsService.send(phone, categoryTemplateMap.get(category), params);
                } catch (Exception e) {
                    logger.error("SMS send exception", e);
                }

                if (sendResult != null) {
                    logger.info("SMS send result = " + JSONUtil.toJsonString(sendResult));
                }

                if (sendResult != null && sendResult.getError_code() == 0) {
                    result.setResult(Response.success(true));
                } else {
                    logger.error("SMS send to phone:{} failure.", phone);
                    result.setResult(Response.error(ErrorCode.CAPTCHA_SEND_FAILURE));
                }
            });
        } catch (RejectedExecutionException e) {
            result.setResult(Response.error(ErrorCode.SYSTEM_OVERLOAD));
        }

        return result;
    }

}
