package com.zhenhui.demo.uac.service.controller;

import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.core.repository.UserRepository;
import com.zhenhui.demo.uac.core.utils.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/me")
    @ResponseBody
    public String me() {

        User user = userRepository.queryUser("18621816233");

        return JSONUtil.toJsonString(user);
    }


}
