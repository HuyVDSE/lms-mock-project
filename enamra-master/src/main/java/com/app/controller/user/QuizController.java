package com.app.controller.user;

import com.app.model.Quiz;
import com.app.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
@RequestMapping("/user/quiz")
public class QuizController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/do_quiz/{sectionId}")
    public ModelAndView DoQuiz(@PathVariable("sectionId") Long sectionId,
                               HttpServletRequest request) {
        HttpSession session = request.getSession();
        Date afterAdding10Mins = null;

        if(session.getAttribute("time") == null) {
            return CreateQuiz(session, sectionId);
        } else {
            afterAdding10Mins = (Date) session.getAttribute("time");
        }

        ModelAndView model = new ModelAndView();
        model.addObject("questionList", questionService.getQuestionsBySectionId(sectionId));
        model.addObject("finish_time", afterAdding10Mins);
        model.setViewName("user/do_quiz");
        return model;
    }

    public ModelAndView CreateQuiz(HttpSession session, Long sectionId) {
        int totalTime = 1;
        Calendar date = Calendar.getInstance();
        long timeInSecs = date.getTimeInMillis();
        Date afterAdding10Mins = new Date(timeInSecs + (totalTime * 60 * 1000));
        session.setAttribute("time", afterAdding10Mins);
        ModelAndView model = new ModelAndView();
        model.addObject("questionList", questionService.getQuestionsBySectionId(sectionId));
        model.addObject("finish_time", afterAdding10Mins);
        model.setViewName("user/do_quiz");
        return model;
    }
}
