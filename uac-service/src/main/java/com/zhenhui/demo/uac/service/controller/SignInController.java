package com.zhenhui.demo.uac.service.controller;

import com.zhenhui.demo.uac.common.ErrorCode;
import com.zhenhui.demo.uac.common.Response;
import com.zhenhui.demo.uac.common.SocialType;
import com.zhenhui.demo.uac.core.dataobject.SocialAccount;
import com.zhenhui.demo.uac.core.dataobject.User;
import com.zhenhui.demo.uac.core.repository.SocialAccountRepository;
import com.zhenhui.demo.uac.core.repository.UserRepository;
import com.zhenhui.demo.uac.core.repository.exception.UserAlreadyExistsException;
import com.zhenhui.demo.uac.service.common.WeiboService;
import com.zhenhui.demo.uac.service.common.WeiboUserInfo;
import com.zhenhui.demo.uac.service.controller.request.Signin;
import com.zhenhui.demo.uac.service.controller.request.WeiboBinding;
import com.zhenhui.demo.uac.service.controller.request.WeiboSignin;
import com.zhenhui.demo.uac.service.manager.CaptchaManager;
import com.zhenhui.demo.uac.service.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

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
    @Qualifier("weibo")
    private Retrofit retrofit;

    @ResponseBody
    @RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @RequestMapping(value = "/login/weibo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<Response<String>> loginWeibo(@RequestBody WeiboSignin signin) {

        final DeferredResult<Response<String>> result = new DeferredResult<>();

        WeiboService weiboService = retrofit.create(WeiboService.class);
        Call<WeiboUserInfo> call = weiboService.getUserInfo(signin.getToken(), String.valueOf(signin.getUid()));
        call.enqueue(new Callback<WeiboUserInfo>() {
            @Override
            public void onFailure(Call<WeiboUserInfo> call, Throwable throwable) {
                result.setResult(Response.error(ErrorCode.UNKNOWN));

            }
            @Override
            public void onResponse(Call<WeiboUserInfo> call, retrofit2.Response<WeiboUserInfo> response) {
                WeiboUserInfo userInfo = response.body();
                if (userInfo == null || userInfo.getError_code() != 0) { // error
                    result.setResult(Response.error(ErrorCode.WEIBO_GET_USER_ERROR));
                } else {
                    result.setResult(doLoginWeibo(userInfo));
                }
            }
        });

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/login/weibo/bind", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response<String> bindWeibo(@RequestBody WeiboBinding binding) {

        final String phone = binding.getPhone();
        final String captcha = captchaManager.lookupCaptcha(phone);

        final User user = userRepository.queryUser(phone);
        if (null == user) {
            return Response.error(ErrorCode.USER_NOT_FOUND);
        }

        if (StringUtils.isEmpty(captcha)) {
            return Response.error(ErrorCode.CAPTCHA_EXPIRED);
        }

        if (!captcha.equals(binding.getCaptcha())) {
            return Response.error(ErrorCode.CAPTCHA_MISMATCH);
        }

        final SocialAccount socialAccount = socialAccountRepository.getSocialAccount(SocialType.WEIBO, binding.getOpenId());
        if (socialAccount == null) {
            return Response.error(ErrorCode.WEIBO_NO_AUTH);
        }

        final Long originUserId = socialAccount.getUserId() != null ? socialAccount.getUserId() : 0L;
        if (originUserId.equals(user.getId())) {
            return Response.success(tokenUtils.createToken(user));
        } else {
            socialAccount.setUserId(user.getId());
        }

        if (null != socialAccountRepository.updateUserId(SocialType.WEIBO, socialAccount.getOpenId(), originUserId, user.getId())) {
            return Response.success(tokenUtils.createToken(user));
        }

        return Response.error(ErrorCode.USER_BIND_ERROR);
    }

    private Response<String> doLoginWeibo(WeiboUserInfo userInfo) {
        SocialAccount socialAccount = new SocialAccount();
        socialAccount.setType(SocialType.WEIBO);
        socialAccount.setOpenId(userInfo.getId());
        socialAccount.setToken("");
        socialAccount.setNickname(userInfo.getName());
        socialAccount.setAvatar(userInfo.getAvatar_hd());
        socialAccount.setActivated(true);

        try {
            socialAccountRepository.createSocialAccount(socialAccount);
        } catch (UserAlreadyExistsException e) {
            socialAccount = socialAccountRepository.getSocialAccount(SocialType.WEIBO, userInfo.getId());
        }

        if (socialAccount.getUserId() > 0) {
            User user = userRepository.queryUser(socialAccount.getUserId());
            if (user != null) {
                return Response.success(tokenUtils.createToken(user));
            }
        }

        return Response.error(ErrorCode.NO_USER_BOUND);
    }

}

