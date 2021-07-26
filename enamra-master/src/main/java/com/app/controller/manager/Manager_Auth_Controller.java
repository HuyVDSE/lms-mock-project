package com.app.controller.manager;


import com.app.model.User;
import com.app.service.ManagerService;
import com.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/manager")
@AllArgsConstructor
public class Manager_Auth_Controller {

    private final ManagerService managerService;
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;


    @GetMapping("/cc")
    public String cc(){return "manager/cc";}

    @GetMapping("/create/student")
    public ModelAndView loadCreateStudentForm(){
        ModelAndView model = new ModelAndView("manager/student_signup");
        model.addObject("user", new User());
        return model;
    }

    @PostMapping("/create/student")
    public ModelAndView createStudent(@Valid User user, BindingResult bindingResult){
        ModelAndView model = new ModelAndView();

        User userExist = userService.findUserByEmail(user.getEmail());
        if (userExist != null){ bindingResult.rejectValue("email", "error.user", "This email already  exists!"); }

        if (!user.getPassword().equals(user.getConfirmPassword())){ bindingResult.rejectValue("password", "error.user", "Password didnt match!!!"); }

        if (bindingResult.hasErrors()) { model.setViewName("manager/student_signup");
        } else {
            managerService.saveChief_Instructor(user);
            model.addObject("msg", "Chief Instructor has been registered successfully!");
            model.addObject("user", new User());
            model.setViewName("manager/student_signup");
        }
        return model;
    }

    @GetMapping("/del/student/{id}")
    public String deleteStudentById(@PathVariable("id") Long id){
        managerService.deleteStudentByID(id);
        return "redirect:/manager_dashboard";
    }

    @GetMapping("/edit/student/{id}")
    public String loadUserDetailForUpdate(@PathVariable("id") Long id, Model model) {
        User currentUser = userService.findUserById(id);
        model.addAttribute("user", currentUser);
        return "manager/student_edit";
    }

    @PostMapping("/edit/student")
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
                return "manager/student_edit";
            }

            userService.saveUser(user, userUpdate.getRoles());
            model.addAttribute("msg", "Update user successfully!");
            model.addAttribute("user", new User());

        }
        return "manager/student_edit";
    }
}
