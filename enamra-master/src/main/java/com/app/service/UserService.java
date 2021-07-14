package com.app.service;

import com.app.model.User;

public interface UserService {

    User findUserByEmail(String email);

    User findUserById(Long id);

    void saveUser(User user);

    void activeUser(User user);

    User findUserByUsername(String username);

}
