package com.app.controller.admin;


import com.app.model.User;
import com.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class Admin_Dash_Controller {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin_Dashboard")
    public ModelAndView adminDashBoard() {
        ModelAndView model = new ModelAndView("admin_cc/admin_dashboard");
        List<User> admins = adminService.getAllAdmins();
        List<User> students = adminService.getAllStudents();
        List<User> teachers = adminService.getAllTeachers();
        model.addObject("AdminList", admins);
        model.addObject("hrList", students);
        model.addObject("managerList", teachers);
        return model;
    }

}
