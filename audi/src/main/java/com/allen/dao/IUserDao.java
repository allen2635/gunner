package com.allen.dao;

import com.allen.model.User;

public interface IUserDao {

    int insertUser(User user);

    User queryUserById(int id);

    User queryUser(String username, String password);

    int deleteUser(int id);
}
