package com.app.service;


import com.app.model.User;

import javax.jws.soap.SOAPBinding;
import java.util.List;

public interface AdminService {

    void saveAdmin(User user);
    void saveStudent(User user);
    void saveTeacher(User user);

    List<User> getAllAdmins();  // testing ok
    List<User> getAllStudents();
    List<User> getAllTeachers();
    void deleteAdminByID(Long id);
    void deleteTeacherByID(Long id);
    void deleteStudentByID(Long id);




}
