package com.app.controller;


import com.app.model.Course;
import com.app.model.User;
import com.app.repository.CourseRepository;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class WelcomeController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ModelAndView welcome(){
        ModelAndView model = new ModelAndView("welcome");
            List<Course> first_four_from_last = courseRepository.recently_added_first_four_course();
            List<Course> second_4_from_last = courseRepository.recently_added_second_four_course();
            model.addObject("recently_4", first_four_from_last);
            model.addObject("second_4", second_4_from_last);
        return model;
    }

    @GetMapping("/home")
    public ModelAndView homePage(Principal principal){
        ModelAndView model = new ModelAndView("home");
        List<Course> first_four_from_last = courseRepository.recently_added_first_four_course();
        User userCC = userService.findUserByEmail(principal.getName());
        String role = String.valueOf(userCC.getRoles());
        role.substring(1,role.length()-1);

        if (role.equals("STUDENT")){
            model.addObject("user", role);
        } else if (role.equals("ADMIN")){
            model.addObject("admin", role);
        } else if (role.equals("TEACHER")){
            model.addObject("manager", role);
        }

        List<Course> second_4_from_last = courseRepository.recently_added_second_four_course();
        model.addObject("recently_4", first_four_from_last);
        model.addObject("second_4", second_4_from_last);
        return model;
    }
}
