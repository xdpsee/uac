package com.zhenhui.demo.uac.service.controller;

import com.zhenhui.demo.uac.common.ErrorCode;
import com.zhenhui.demo.uac.common.Principal;
import com.zhenhui.demo.uac.common.Response;
import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.core.repository.UserRepository;
import com.zhenhui.demo.uac.security.auth.TokenBasedAuthentication;
import com.zhenhui.demo.uac.service.controller.response.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @ResponseBody
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/me", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response<UserInfo> info() {

        TokenBasedAuthentication authentication = (TokenBasedAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Principal principal = (Principal) authentication.getPrincipal();

        User user = userRepository.queryUser(principal.getUserId());
        if (null == user) {
            return Response.error(ErrorCode.USER_NOT_FOUND);
        }

        return Response.success(new UserInfo(user.getId(), user.getNickname(), user.getPhone(), user.getAvatar()));
    }

    @ResponseBody
    @RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response<Boolean> testPhone(@RequestParam("phone") String phone) {
        User user = userRepository.queryUser(phone);
        return Response.success(user != null);
    }


}
