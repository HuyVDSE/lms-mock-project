package com.app.service.impl;

import com.app.model.Role;
import com.app.model.User;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(0);
        Role userRule = roleRepository.findByRole("STUDENT");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRule)));
        userRepository.save(user);
    }

    @Override
    public void saveUser(User user, Set<Role> role) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(1);
        user.setRoles(role);
        userRepository.save(user);
    }

    @Override
    public void activeUser(User user) {
        user.setActive(1);
        userRepository.save(user);
    }

    @Override
    public void changePassword(String email, String pass) {
        User user = userRepository.findByEmail(email);
        user.setPassword(encoder.encode(pass));
        userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
