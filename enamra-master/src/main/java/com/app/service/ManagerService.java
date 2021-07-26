package com.app.service;

import com.app.model.User;

import java.util.List;

public interface ManagerService {
    void saveChief_Instructor(User user);

    List<User> getAllStudents();

    void deleteStudentByID(Long id);
}
