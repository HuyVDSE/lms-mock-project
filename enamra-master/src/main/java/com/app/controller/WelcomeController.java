package com.app.controller;


import com.app.model.Course;
import com.app.model.User;
import com.app.repository.CourseRepo;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class WelcomeController {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public ModelAndView welcome(){
        ModelAndView model = new ModelAndView("welcome");
            List<Course> first_four_from_last = courseRepo.recently_added_first_four_course();
            List<Course> second_4_from_last = courseRepo.recently_added_second_four_course();
            model.addObject("recently_4", first_four_from_last);
            model.addObject("second_4", second_4_from_last);
        return model;
    }

    @GetMapping("/home")
    public ModelAndView homePage(Principal principal){
        ModelAndView model = new ModelAndView("home");
        List<Course> first_four_from_last = courseRepo.recently_added_first_four_course();
        User userCC = userService.findUserByEmail(principal.getName());
        String role = String.valueOf(userCC.getRoles());
        role.substring(1,role.length()-1);

        if (role.equals("USER")){
            model.addObject("user", role);
        }else if (role.equals("ADMIN")){
            model.addObject("admin", role);
        }else if (role.equals("HR")){
            model.addObject("hr", role);
        }else if (role.equals("MANAGER")){
            model.addObject("manager", role);
        }else if (role.equals("CHIF INSTRUCTOR")){
            model.addObject("CHIF_INSTRUCTOR", role);
        }else if (role.equals("INSTRUCTOR")){
            model.addObject("INSTRUCTOR", role);
        }else if (role.equals("Assistant INSTRUCTOR")){
            model.addObject("Assistant_INSTRUCTOR", role);
        }else if (role.equals("Teaching Assistant")){
            model.addObject("Teaching_Assistant", role);
        }else if (role.equals("STUFF")){
            model.addObject("STUFF", role);
        }else if (role.equals("EMPLOYEE")){
            model.addObject("EMPLOYEE", role);
        }

        List<Course> second_4_from_last = courseRepo.recently_added_second_four_course();
        model.addObject("recently_4", first_four_from_last);
        model.addObject("second_4", second_4_from_last);
        return model;
    }




}
