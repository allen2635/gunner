package com.allen.audi.activity;

import android.view.View;

import com.allen.audi.R;
import com.allen.audi.base.BaseActivity;
import com.allen.audi.http.YmApiRequest;
import com.allen.audi.http.api.ApiLogin;
import com.allen.audi.http.response.LoginResponse;
import com.allen.audi.widget.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @className:
 * @classDescription:
 * @Author: allen
 * @createTime: 2018/3/8.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.activity_login_username)
    ClearEditText usernameTv;

    @BindView(R.id.activity_login_password)
    ClearEditText passwordTv;


    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {

    }

    @OnClick({R.id.act_login_tv_register, R.id.activity_login_btn_login})
    void onClick(View v) {

        switch (v.getId()) {

            case R.id.act_login_tv_register:


                break;

            case R.id.activity_login_btn_login:

                login();
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {

        ApiLogin apiLogin = YmApiRequest.getInstance().create(ApiLogin.class);
//        String params = new YmRequestParameters(ApiLogin.USER_REGISTER_PARAMS, usernameTv.getText().toString(),
//                passwordTv.getText().toString()).toString();
        Call<LoginResponse> call = apiLogin.login(usernameTv.getText().toString(),
                passwordTv.getText().toString());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body() != null && response.body().isSuccess()) {
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }
}
