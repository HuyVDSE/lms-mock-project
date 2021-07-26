package com.app.controller.admin;


import com.app.model.User;
import com.app.service.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/home")
    public String homeAdmin() {
        return "admin/home";
    }


    @GetMapping("/signup")
    public ModelAndView createadminPage() {
        ModelAndView model = new ModelAndView();
        model.addObject("user", new User());
        model.setViewName("admin/signup");
        return model;
    }

    @PostMapping("/signup")
    public ModelAndView signup(@Valid User user, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("password", "error.user", "Password not match!!!!");
        }

        if (bindingResult.hasErrors()) {
            model.setViewName("admin/signup");
        } else {
            adminService.saveAdmin(user);
            model.addObject("msg", "User has been registered successfully!");
            model.addObject("user", new User());
            model.setViewName("admin/signup");
        }
        return model;

    }


    @GetMapping("/list")
    public ModelAndView adminList() {
        ModelAndView model = new ModelAndView("admin/home");
        List<User> adminList = adminService.getAllAdmins();
        model.addObject("adminLIST", adminList);
        return model;
    }

    @GetMapping("/edit/{id}")
    public String loadUserDetailForUpdate(@PathVariable("id") Long id, Model model) {
        User currentUser = userService.findUserById(id);
        model.addAttribute("user", currentUser);
        return "admin/edit";
    }

    @PostMapping("/edit")
    public String updateUserDetail(@Valid User user, BindingResult bindingResult,
                                   HttpServletRequest request, Model model) {
        User userUpdate = userService.findUserByEmail(user.getEmail());
        String oldPassword = request.getParameter("oldPassword");

        if (user.getFirstname().equals("")) {
            bindingResult.rejectValue("firstname", "error.user", "First name must not empty!");
        }else if(!(user.getFirstname().matches("[a-zA-Z]+"))){
            bindingResult.rejectValue("firstname", "error.user", "First name must not empty!");
        }
        else if (user.getLastname().equals("")) {
            bindingResult.rejectValue("lastname", "error.user", "Last name must not empty!");

        } else if(!(user.getFirstname().matches("[a-zA-Z]+"))){
            bindingResult.rejectValue("lastname", "error.user", "Last name must not empty!");
        }

        else if (oldPassword == null || !encoder.matches(oldPassword, userUpdate.getPassword())) {
            model.addAttribute("passworderror", "Current password didn't match!!!!");
        } else if (user.getPassword().equals("")) {
            bindingResult.rejectValue("password", "error.user", "Password must not empty!");
        } else {
            if (!user.getPassword().equals(user.getConfirmPassword())) {
                bindingResult.rejectValue("password", "error.user", "Confirm Password didnt match!!!!");
            }

            if (bindingResult.hasErrors()) {
                model.addAttribute("user", userUpdate);
                return "admin/edit";
            }
            user.setId(userUpdate.getId());
            userService.saveUser(user, userUpdate.getRoles());
            model.addAttribute("msg", "Update user successfully!");
            model.addAttribute("user", new User());

        }
        return "admin/edit";
    }
}
