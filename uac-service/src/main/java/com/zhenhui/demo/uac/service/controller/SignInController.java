package com.zhenhui.demo.uac.service.controller;

import com.zhenhui.demo.uac.common.ErrorCode;
import com.zhenhui.demo.uac.common.Principal;
import com.zhenhui.demo.uac.common.Response;
import com.zhenhui.demo.uac.common.SocialType;
import com.zhenhui.demo.uac.core.dataobject.SocialAccount;
import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.core.repository.SocialAccountRepository;
import com.zhenhui.demo.uac.core.repository.UserRepository;
import com.zhenhui.demo.uac.core.repository.exception.UserAlreadyExistsException;
import com.zhenhui.demo.uac.security.auth.TokenBasedAuthentication;
import com.zhenhui.demo.uac.service.controller.request.Signin;
import com.zhenhui.demo.uac.service.controller.request.SocialBinding;
import com.zhenhui.demo.uac.service.controller.request.SocialSignin;
import com.zhenhui.demo.uac.service.controller.request.SocialUnbinding;
import com.zhenhui.demo.uac.service.controller.request.UserBinding;
import com.zhenhui.demo.uac.service.manager.CaptchaManager;
import com.zhenhui.demo.uac.service.manager.SocialSigninManager;
import com.zhenhui.demo.uac.service.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@SuppressWarnings("unchecked")
@RestController
@RequestMapping("/auth")
public class SignInController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SocialAccountRepository socialAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CaptchaManager captchaManager;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private SocialSigninManager socialSigninManager;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response<String> login(@RequestBody Signin signin) {

        final User user = userRepository.queryUser(signin.getPhone());
        if (user != null) {
            if (passwordEncoder.matches(signin.getSecret(), user.getSecret())) {
                return Response.success(tokenUtils.createToken(user));
            }

            return Response.error(ErrorCode.SECRET_NOT_MATCH);
        }

        return Response.error(ErrorCode.USER_NOT_FOUND);
    }

    @ResponseBody
    @RequestMapping(value = "/login/social", method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<Response<String>> loginSocial(@RequestBody SocialSignin signin) {

        final DeferredResult<Response<String>> result = new DeferredResult<>();

        socialSigninManager.socialLogin(signin, result);

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/login/social/bind", method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response<String> bindSocial(@RequestBody UserBinding binding) {

        final String phone = binding.getPhone();
        final String captcha = captchaManager.lookupCaptcha(phone);
        if (StringUtils.isEmpty(captcha)) {
            return Response.error(ErrorCode.CAPTCHA_EXPIRED);
        }

        if (!captcha.equals(binding.getCaptcha())) {
            return Response.error(ErrorCode.CAPTCHA_MISMATCH);
        }

        final SocialAccount socialAccount = socialAccountRepository.getSocialAccount(binding.getType(),
            binding.getOpenId());
        if (socialAccount == null) {
            return Response.error(ErrorCode.SOCIAL_NO_AUTH);
        }

        User user;
        try {
            user = userRepository.createUser(binding.getPhone(), binding.getSecret());
        } catch (UserAlreadyExistsException e) {
            user = userRepository.queryUser(phone);
            if (!passwordEncoder.matches(binding.getSecret(), user.getSecret())) {
                return Response.error(ErrorCode.SECRET_NOT_MATCH);
            }
        }

        final Long originUserId = socialAccount.getUserId() != null ? socialAccount.getUserId() : 0L;
        if (originUserId.equals(user.getId())) {
            return Response.success(tokenUtils.createToken(user));
        } else {
            socialAccount.setUserId(user.getId());
        }

        if (null != socialAccountRepository.updateUserId(binding.getType(), socialAccount.getOpenId(), originUserId,
            user.getId())) {
            return Response.success(tokenUtils.createToken(user));
        }

        return Response.error(ErrorCode.SOCIAL_USER_BIND_ERROR);
    }

    @ResponseBody
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/social/bind", method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response<Boolean> socialBind(@RequestBody SocialBinding binding) {

        TokenBasedAuthentication authentication = (TokenBasedAuthentication)SecurityContextHolder.getContext()
            .getAuthentication();
        Principal principal = (Principal)authentication.getPrincipal();

        User user = userRepository.queryUser(principal.getUserId());
        if (null == user) {
            return Response.error(ErrorCode.USER_NOT_FOUND);
        }

        SocialAccount socialAccount = new SocialAccount(binding.getType()
            , binding.getOpenId()
            , binding.getNickname()
            , binding.getAvatar());

        try {
            socialAccount.setUserId(user.getId());
            socialAccountRepository.createSocialAccount(socialAccount);
            return Response.success(true);

        } catch (UserAlreadyExistsException e) {
            socialAccount = socialAccountRepository.getSocialAccount(SocialType.WEIBO, binding.getOpenId());
        }

        if (socialAccount.getUserId() == 0) {
            if (null != socialAccountRepository.updateUserId(binding.getType(), binding.getOpenId(), 0, user.getId())) {
                return Response.success(true);
            }
        } else if (socialAccount.getUserId().equals(user.getId())) {
            return Response.success(true);
        }

        return Response.error(ErrorCode.SOCIAL_USER_BIND_ERROR);
    }

    @ResponseBody
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/social/unbind", method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response<Boolean> socialUnbind(@RequestBody SocialUnbinding unbinding) {

        TokenBasedAuthentication authentication = (TokenBasedAuthentication)SecurityContextHolder.getContext()
            .getAuthentication();
        final Principal principal = (Principal)authentication.getPrincipal();
        final User user = userRepository.queryUser(principal.getUserId());
        if (null == user) {
            return Response.error(ErrorCode.USER_NOT_FOUND);
        }

        final SocialAccount socialAccount = socialAccountRepository.getSocialAccount(unbinding.getType(),
            unbinding.getOpenId());
        if (null == socialAccount || !user.getId().equals(socialAccount.getUserId())) {
            return Response.error(ErrorCode.SOCIAL_NO_USER_BOUND);
        }

        if (null != socialAccountRepository.updateUserId(unbinding.getType(), unbinding.getOpenId(), user.getId(), 0)) {
            return Response.success(true);
        }

        return Response.error(ErrorCode.SOCIAL_UNBIND_ERROR);
    }

}

