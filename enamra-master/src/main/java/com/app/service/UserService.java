package com.app.service;

import com.app.model.Role;
import com.app.model.User;

import java.util.Set;

public interface UserService {

    User findUserByEmail(String email);

    User findUserById(Long id);

    void saveUser(User user);

    void saveUser(User user, Set<Role> role);

    void activeUser(User user);

    void changePassword(String email, String pass);

    User findUserByUsername(String username);

}
