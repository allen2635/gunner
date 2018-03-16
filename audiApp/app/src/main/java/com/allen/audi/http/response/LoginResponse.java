package com.allen.audi.http.response;

import com.allen.audi.http.BaseResponse;

/**
 * @className:
 * @classDescription:
 * @Author: allen
 * @createTime: 2018/3/12.
 */
public class LoginResponse extends BaseResponse {

    private String username;

    private String userId;

    private String nickname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
