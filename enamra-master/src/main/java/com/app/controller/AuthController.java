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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Random;

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
        ModelAndView model = new ModelAndView();
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
        if(usercc == null) {
            model.setViewName("/user/login");
            return model;
        }
        String role = String.valueOf(usercc.getRoles());
        role.substring(1, role.length()-1);
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
        String code = request.getParameter("code");
        HttpSession session = request.getSession();
        String random = (String) session.getAttribute("CODE");
        if(code != null){
            if(code.equals(random)) {
                User usercc = userService.findUserByEmail(email);
                userService.activeUser(usercc);
                session.setAttribute("CODE", null);
            }
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("user/login");
        return model;
    }

    @PostMapping("/user/settings/checkVerify")
    public String checkVerify(HttpServletRequest request){
        String email = request.getParameter("email");
        User usercc = userService.findUserByEmail(email);
        if(usercc != null) {
            int active = usercc.getActive();
            if(active == 0) {
                HttpSession session = request.getSession();
                Random rnd = new Random();
                int leftLimit = 97; // letter 'a'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 100;
                Random rndd = new Random();
                String generatedString = rndd.ints(leftLimit, rightLimit + 1)
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
                int number = rnd.nextInt(999999999);
                String random = String.format("%09d", number);
                session.setAttribute("CODE", random + generatedString);
                session.setMaxInactiveInterval(60*5);
                sendMailService.sendMail(email, random + generatedString, "VERIFY");
                return "redirect:/user/verify";
            }
        }
        return "redirect:/user/login?error=true";
    }

    @GetMapping("/user/verify")
    public ModelAndView verifyPage(){
        ModelAndView model = new ModelAndView();
        model.setViewName("/user/verify");
        return model;
    }

    @GetMapping("/user/forgot")
    public ModelAndView forgotPage(){
        ModelAndView model = new ModelAndView();
        model.setViewName("/user/forgot");
        return model;
    }

    @GetMapping("/user/reset_password")
    public ModelAndView resetPassword(HttpServletRequest request){
        ModelAndView model = new ModelAndView();
        model.addObject("user", new User());
        String code = request.getParameter("code");
        HttpSession session = request.getSession();
        String random = (String) session.getAttribute("CODE");
        if(code != null){
            if(code.equals(random)) {
                session.setAttribute("CODE", null);
                model.setViewName("/user/reset_password");
                return model;
            }
        }
        model.setViewName("/user/login");
        return model;
    }

    @PostMapping("/user/reset_password")
    public ModelAndView changePassword(@Valid User user, BindingResult bindingResult){
        ModelAndView model = new ModelAndView();
        if(user.getPassword() == "") {
            bindingResult.rejectValue("password", "error.user", "Password must not empty!");
        } else {
            if (!user.getPassword().equals(user.getConfirmPassword())){
                bindingResult.rejectValue("password", "error.user", "Password didnt match!!!!");
            }
            if (bindingResult.hasErrors()) {
                model.setViewName("/user/reset_password");
            } else {
                userService.changePassword(user.getEmail(), user.getPassword());
                model.addObject("msg", "Reset password successfully!");
                model.addObject("user", new User());
                model.setViewName("/user/reset_password");
            }
        }
        return model;
    }

    @PostMapping("/user/request_reset")
    public String requestReset(HttpServletRequest request){
        String email = request.getParameter("email");
        User usercc = userService.findUserByEmail(email);
        if(usercc != null) {
            HttpSession session = request.getSession();
            Random rnd = new Random();
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 100;
            Random rndd = new Random();
            String generatedString = rndd.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            int number = rnd.nextInt(999999999);
            String random = String.format("%09d", number);
            session.setAttribute("CODE", random + generatedString);
            session.setMaxInactiveInterval(60*5);
            sendMailService.sendMail(email, random + generatedString, "RESET_PASSWORD");
        }
        return "/user/reset";
    }

}
