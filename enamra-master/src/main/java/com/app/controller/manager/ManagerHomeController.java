package com.app.controller.manager;


import com.app.model.User;
import com.app.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ManagerHomeController {

    @Autowired
    private ManagerService managerService;

    @GetMapping("/manager_dashboard")
    public ModelAndView loadDashboard(){
        ModelAndView model = new ModelAndView("manager/manager_dashboard");
        List<User> students = managerService.getAllStudents();
        model.addObject("studentList", students);
        return model;
    }
}
