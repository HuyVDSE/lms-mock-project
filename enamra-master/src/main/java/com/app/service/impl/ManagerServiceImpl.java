package com.app.service.impl;

import com.app.model.Role;
import com.app.model.User;
import com.app.repository.ManagerRepo;
import com.app.repository.RoleRepository;
import com.app.service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepo managerRepo;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public void saveChief_Instructor(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(1);
        Role studentRole = roleRepository.findByRole("STUDENT");
        user.setRoles(new HashSet<Role>(Arrays.asList(studentRole)));
        managerRepo.save(user);
    }

    @Override
    public List<User> getAllStudents() {
        return managerRepo.findAllStudentByRoles();
    }

    @Override
    public void deleteStudentByID(Long id) {
        managerRepo.deleteById(id);
    }
}
