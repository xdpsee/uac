package com.zhenhui.demo.uac.service.controller;

import com.zhenhui.demo.uac.common.ErrorCode;
import com.zhenhui.demo.uac.common.Principal;
import com.zhenhui.demo.uac.common.Response;
import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.core.repository.UserRepository;
import com.zhenhui.demo.uac.security.utils.SecurityUtils;
import com.zhenhui.demo.uac.service.controller.request.Signin;
import com.zhenhui.demo.uac.service.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/user")
public class SignInController {

    @Autowired
    private UserRepository userRepository;

    @ResponseBody
    @RequestMapping(value = "/signin", produces = "application/json; charset=utf-8")
    public Response<String> login(@RequestBody Signin signin) {

        final User user = userRepository.queryUser(signin.getPhone());
        if (user != null) {
            if (user.getSecret().equals(signin.getSecret())) {
                return Response.success(TokenUtils.createToken(user));
            }

            return Response.error(ErrorCode.SECRET_NOT_MATCH);
        }

        return Response.error(ErrorCode.USER_NOT_FOUND);
    }

    @ResponseBody
    @RequestMapping(value = "/test", produces = "application/json; charset=utf-8")
    public Response<Principal> test(@RequestParam("token") String token) {

        try {
            Principal principal = SecurityUtils.parseToken(token);

            return Response.success(principal);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.error(ErrorCode.TOKEN_INVALID);
    }
}
