package com.zhenhui.demo.uac.service.controller;

import com.zhenhui.demo.uac.common.Principal;
import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.core.repository.UserRepository;
import com.zhenhui.demo.uac.common.JSONUtil;
import com.zhenhui.demo.uac.security.auth.TokenBasedAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @ResponseBody
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String info() {

        TokenBasedAuthentication authentication = (TokenBasedAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Principal principal = (Principal) authentication.getPrincipal();

        User user = userRepository.queryUser(principal.getUserId());

        return JSONUtil.toJsonString(user);
    }


}
