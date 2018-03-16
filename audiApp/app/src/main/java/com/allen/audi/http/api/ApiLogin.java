package com.allen.audi.http.api;

import com.allen.audi.http.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @className:
 * @classDescription:
 * @Author: allen
 * @createTime: 2018/3/8.
 */
public interface ApiLogin {

    String[] USER_REGISTER_PARAMS = {

            "username", "password"
    };

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );
}
