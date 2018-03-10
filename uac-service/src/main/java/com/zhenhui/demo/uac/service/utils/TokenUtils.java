package com.zhenhui.demo.uac.service.utils;

import com.zhenhui.demo.uac.common.Principal;
import com.zhenhui.demo.uac.common.SocialType;
import com.zhenhui.demo.uac.core.dataobject.SocialAccount;
import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.security.utils.SecurityUtils;
import com.zhenhui.demo.uac.service.common.Constants;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class TokenUtils {

    public static String createToken(User user) {

        Principal principal = new Principal();
        principal.setUserId(user.getId());
        principal.setPhone(user.getPhone());
        principal.setType(SocialType.NONE);
        principal.setAuthorities(user.getAuthorities());

        return SecurityUtils.createToken(principal
                , TimeUnit.SECONDS.toMillis(Constants.TOKEN_EXPIRES_SECONDS));
    }

    public static String createToken(SocialAccount socialAccount) {

        Principal principal = new Principal();
        principal.setUserId(socialAccount.getId());
        principal.setType(socialAccount.getType());
        principal.setOpenId(socialAccount.getOpenId());
        principal.setAuthorities(Arrays.asList("USER"));

        return SecurityUtils.createToken(principal
                , TimeUnit.SECONDS.toMillis(Constants.TOKEN_EXPIRES_SECONDS));
    }

}
