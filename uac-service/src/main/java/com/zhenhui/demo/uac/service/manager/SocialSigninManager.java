package com.zhenhui.demo.uac.service.manager;

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
import com.zhenhui.demo.uac.service.controller.request.SocialSignin;
import com.zhenhui.demo.uac.service.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import retrofit2.Call;
import retrofit2.Callback;

@SuppressWarnings("unchecked")
@Component
public class SocialSigninManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SocialAccountRepository socialAccountRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private WeiboService weiboService;

    public void socialLogin(SocialSignin signin, final DeferredResult<Response<String>> result) {

        switch (signin.getType()) {
            case WEIBO: {
                weiboService.getUserInfo(signin.getToken(), String.valueOf(signin.getUid()))
                    .enqueue(new Callback<WeiboUserInfo>() {
                        @Override
                        public void onFailure(Call<WeiboUserInfo> call, Throwable throwable) {
                            result.setResult(Response.error(ErrorCode.UNKNOWN));
                        }

                        @Override
                        public void onResponse(Call<WeiboUserInfo> call, retrofit2.Response<WeiboUserInfo> response) {
                            WeiboUserInfo userInfo = response.body();
                            if (userInfo == null || userInfo.getError_code() != 0) { // error
                                result.setResult(Response.error(ErrorCode.SOCIAL_GET_USER_ERROR));
                            } else {
                                result.setResult(doLoginWeibo(userInfo));
                            }
                        }
                    });
            }
            break;
            default: {
                result.setResult(Response.error(ErrorCode.UNKNOWN));
            }
            break;
        }

    }

    private Response<String> doLoginWeibo(WeiboUserInfo userInfo) {
        SocialAccount socialAccount = new SocialAccount(SocialType.WEIBO
            , userInfo.getId()
            , userInfo.getName()
            , userInfo.getAvatar_hd());

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

        return Response.error(ErrorCode.SOCIAL_NO_USER_BOUND);
    }

}
