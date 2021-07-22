package com.app.controller.manager;


import com.app.model.Quiz;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/manager/quiz")
@AllArgsConstructor
public class QuizManagerController {
    @GetMapping("/create_quiz")
    public ModelAndView ViewPage(HttpServletRequest request){
        String msg = request.getParameter("msg");
        ModelAndView model = new ModelAndView();
        model.setViewName("manager/QuizManager");
        if (msg==null||msg.equals("")){

        } else {
            model.addObject("msg",msg);
        }
        return model;
    }
    @GetMapping("/create_new_quiz")
    public ModelAndView CreateQuiz(HttpServletRequest request){
        ModelAndView model = new ModelAndView();
        model.addObject("quiz", new Quiz());
        model.setViewName("manager/CreateQuiz");
        return model;
    }
}
