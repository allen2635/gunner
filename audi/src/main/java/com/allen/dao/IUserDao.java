package com.allen.dao;

import com.allen.model.User;

public interface IUserDao {

    int insertUser(User user);

    User queryUser(int id);

    int deleteUser(int id);
}
