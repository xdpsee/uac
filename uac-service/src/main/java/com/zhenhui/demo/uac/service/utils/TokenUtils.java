package com.zhenhui.demo.uac.service.utils;

import com.zhenhui.demo.uac.common.Principal;
import com.zhenhui.demo.uac.common.SocialType;
import com.zhenhui.demo.uac.core.dataobject.SocialAccount;
import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@SuppressWarnings("unused")
@Component
public class TokenUtils {

    @Autowired
    private SecurityUtils securityUtils;

    public String createToken(User user) {

        Principal principal = new Principal();
        principal.setUserId(user.getId());
        principal.setPhone(user.getPhone());
        principal.setType(SocialType.NONE);
        principal.setAuthorities(user.getAuthorities());

        return securityUtils.createToken(principal);
    }

    public String createToken(SocialAccount socialAccount) {

        Principal principal = new Principal();
        principal.setUserId(socialAccount.getId());
        principal.setType(socialAccount.getType());
        principal.setOpenId(socialAccount.getOpenId());
        principal.setAuthorities(Arrays.asList("USER"));

        return securityUtils.createToken(principal);
    }

}
