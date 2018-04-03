package com.zhenhui.demo.uac.service.common;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeiboService {

    @GET("users/show.json")
    Call<WeiboUserInfo> getUserInfo(@Query("access_token") String accessToken, @Query("uid") String uid);

}
