package com.app.controller;

import com.app.google.GooglePojo;
import com.app.google.GoogleUtils;
import com.app.model.User;
import com.app.service.SendMailService;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class AuthController {

    @Autowired
    private GoogleUtils googleUtils;

    @Autowired
    private UserService userService;
    @Autowired
    private SendMailService sendMailService;

    @GetMapping("/error")
    public String error(){return "user/error";}



    @GetMapping("/user/home")
    public String home(){return "/user/home";}

    @GetMapping("/user/login")
    public ModelAndView signin(){
        ModelAndView model = new ModelAndView();
        model.setViewName("user/login");
        return model;
    }

    @RequestMapping("/signin-google")
    public ModelAndView signinEmail(HttpServletRequest request) throws IOException {
        String code = request.getParameter("code");
        String accessToken = googleUtils.getToken(code);
        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
        UserDetails userDetail = googleUtils.buildUser(googlePojo);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetail, null,
                    userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User usercc = userService.findUserByEmail(googlePojo.getEmail());
        String role = String.valueOf(usercc.getRoles());
        role.substring(1, role.length()-1);
        ModelAndView model = new ModelAndView();
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
        model.setViewName("home");
        return model;
    }



    @GetMapping("/user/signup")
    public ModelAndView signupPage(){
        ModelAndView model = new ModelAndView();
        model.addObject("user", new User());
        model.setViewName("user/signup");
        return model;
    }


    @PostMapping("/user/signup")
    public ModelAndView signup(@Valid User user, BindingResult bindingResult){
        ModelAndView model = new ModelAndView();
        if(user.getFirstname() == "") {
            bindingResult.rejectValue("firstname", "error.user", "First name must not empty!");
        } else if(user.getLastname() == "") {
            bindingResult.rejectValue("lastname", "error.user", "Last name must not empty!");
        } else if(user.getUsername() == "") {
            bindingResult.rejectValue("username", "error.user", "Username must not empty!");
        } else if(user.getEmail() == "") {
            bindingResult.rejectValue("email", "error.user", "Email must not empty!");
        } else if(user.getPassword() == "") {
            bindingResult.rejectValue("password", "error.user", "Password must not empty!");
        } else {
            User userExist = userService.findUserByEmail(user.getEmail());
            if (userExist != null){
                bindingResult.rejectValue("email", "error.user", "This email already exists!");
            }
            if (!user.getPassword().equals(user.getConfirmPassword())){
                bindingResult.rejectValue("password", "error.user", "Password didnt match!!!!");
            }

            if (bindingResult.hasErrors()) {
                model.setViewName("user/signup");
            } else {
                userService.saveUser(user);
                model.addObject("msg", "User has been registered successfully!");
                model.addObject("user", new User());
                model.setViewName("user/signup");
            }
        }
        return model;
    }

    @GetMapping("/user/settings/verify")
    public ModelAndView verify(HttpServletRequest request){
        String email = request.getParameter("email");
        User usercc = userService.findUserByEmail(email);
        userService.activeUser(usercc);
        ModelAndView model = new ModelAndView();
        model.setViewName("user/verify");
        return model;
    }

}
