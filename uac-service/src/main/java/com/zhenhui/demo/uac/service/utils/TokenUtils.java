package com.zhenhui.demo.uac.service.utils;

import com.zhenhui.demo.uac.common.AuthInfo;
import com.zhenhui.demo.uac.common.SocialType;
import com.zhenhui.demo.uac.core.dataobject.SocialAccount;
import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.security.SecurityUtils;
import com.zhenhui.demo.uac.service.common.Constants;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class TokenUtils {

    public static String createToken(User user) {

        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserId(user.getId());
        authInfo.setPhone(user.getPhone());
        authInfo.setType(SocialType.NONE);

        return SecurityUtils.INSTANCE.createToken(authInfo
                , new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(Constants.TOKEN_EXPIRES_SECONDS)));
    }

    public static String createToken(SocialAccount socialAccount) {

        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserId(socialAccount.getId());
        authInfo.setType(socialAccount.getType());
        authInfo.setOpenId(socialAccount.getOpenId());

        return SecurityUtils.INSTANCE.createToken(authInfo
                , new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(Constants.TOKEN_EXPIRES_SECONDS)));
    }

    public static long
}
