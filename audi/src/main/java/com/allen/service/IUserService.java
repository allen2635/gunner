package com.allen.service;

import com.allen.model.User;

public interface IUserService {

    User queryUserById(int id);

    User queryUser(String username, String password);

    int insertUser(User user);

    int deleteUser(int id);
}
