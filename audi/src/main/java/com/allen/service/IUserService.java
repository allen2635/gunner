package com.allen.service;

import com.allen.model.User;

public interface IUserService {

    User queryUser(int id);

    int insertUser(User user);

    int deleteUser(int id);
}
