package com.app.service.impl;


import com.app.model.Role;
import com.app.model.User;
import com.app.repository.AdminRepo;
import com.app.repository.RoleRepository;
import com.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Qualifier("adminRepo")
    @Autowired
    private AdminRepo adminRepo;

    @Override
    public void saveAdmin(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(0);
        Role admin_role = roleRepository.findByRole("ADMIN");
        user.setRoles(new HashSet<Role>(Arrays.asList(admin_role)));
        adminRepo.save(user);
    }

    @Override
    public void saveStudent(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(0);
        Role studentRole = roleRepository.findByRole("STUDENT");
        user.setRoles(new HashSet<Role>(Arrays.asList(studentRole)));
        adminRepo.save(user);
    }

    @Override
    public void saveTeacher(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(0);
        Role teacherRole = roleRepository.findByRole("TEACHER");
        user.setRoles(new HashSet<Role>(Arrays.asList(teacherRole)));
        adminRepo.save(user);
    }

    @Override
    public List<User> getAllStudents() {
        return adminRepo.findAllStudents();
    }

    @Override
    public List<User> getAllTeachers() {
        return adminRepo.findAllTeachers();
    }

    @Override
    public List<User> getAllAdmins() {
        return adminRepo.findAllAdmins();
    }

    @Override
    public void deleteAdminByID(Long id) {
        adminRepo.deleteById(id);
    }

    @Override
    public void deleteTeacherByID(Long id) {
        adminRepo.deleteById(id);
    }

    @Override
    public void deleteStudentByID(Long id) {
        adminRepo.deleteById(id);
    }
}
