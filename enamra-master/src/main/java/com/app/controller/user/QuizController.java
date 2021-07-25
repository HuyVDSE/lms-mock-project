package com.app.controller.user;

import com.app.model.*;
import com.app.repository.MarkRepo;
import com.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/user/quiz")
public class QuizController {

    @Autowired
    private ResultService resultService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @Autowired
    private MarkService markService;

    @Autowired
    private QuestionForQuizService questionForQuizService;

    @GetMapping("/do_quiz/{quizId}")
    public ModelAndView DoQuiz(@PathVariable("quizId") int quizId,
                               HttpServletRequest request) {
        HttpSession session = request.getSession();
        Date total_time = null;

        if(session.getAttribute("time") == null) {
            return CreateQuiz(session, quizId);
        } else {
            total_time = (Date) session.getAttribute("time");
        }

        ModelAndView model = new ModelAndView();
        List<Question> questionList = (List<Question>) session.getAttribute("questionList");
        model.addObject("questionList", questionList);
        model.addObject("quizId", quizId);
        model.addObject("quiz_size", questionList.size());
        model.addObject("finish_time", total_time);
        model.setViewName("user/do_quiz");
        return model;
    }

    public ModelAndView CreateQuiz(HttpSession session, int quizId) {
        Quiz quiz = quizService.getQuizByQuizId(quizId);
        int totalTime = quiz.getTotalTime();
        Calendar date = Calendar.getInstance();
        long timeInSecs = date.getTimeInMillis();
        Date total_time = new Date(timeInSecs + (totalTime * 60 * 1000));
        session.setAttribute("time", total_time);
        ModelAndView model = new ModelAndView();
        List<Question> questionList = new ArrayList<>();
        for (QuestionForQuiz question: quiz.getQuestionForQuizList()) {
            questionList.add(question.getQuestion());
        }
        session.setAttribute("questionList", questionList);
        model.addObject("questionList", questionList);
        model.addObject("quizId", quizId);
        model.addObject("quiz_size", questionList.size());
        model.addObject("finish_time", total_time);
        model.setViewName("user/do_quiz");
        return model;
    }

    @PostMapping("/submit")
    public ModelAndView Submit(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.findUserByEmail(request.getParameter("user_email"));
        Quiz quiz = quizService.getQuizByQuizId(Integer.parseInt(request.getParameter("quiz_id")));
        List<QuestionForQuiz> questionForQuizList = questionForQuizService.getAllByQuizId(quiz.getQuizId());
        int size = Integer.parseInt(request.getParameter("size_question"));
        List<Question> questionList = (List<Question>) session.getAttribute("questionList");
        String answer[] = new String[size];
        for (int i = 1; i <= size; i++) {
            if(request.getParameter("answer" + i) != null) {
                answer[i - 1] = request.getParameter("answer" + i);
            } else {
                answer[i - 1] = "Not answer";
            }
        }
        float mark = 0;
        float max = 10;
        for (Question question : questionList) {
            for(int i = 0; i < size; i++) {
                for(Answer answerInList : question.getAnswerList()) {
                    if(answerInList.getAnswer().equals(answer[i]) && answerInList.isStatus() == true) {
                        mark = mark + max/size;
                    }
                }
            }
        }

        Mark totalMark = new Mark();
        totalMark.setQuiz(quiz);
        totalMark.setUser(user);
        totalMark.setMark(Math.round(mark*100.0)/100.0);
        markService.save(totalMark);

        for (int i = 0; i < size; i++) {
            Result result = new Result();
            result.setQuestionForQuiz(questionForQuizList.get(i));
            result.setUser(user);
            result.setAnswer(answer[i]);
            result.setMark(totalMark);
            resultService.saveResult(result);
        }

        ModelAndView model = new ModelAndView();
        model.setViewName("/home");
        session.removeAttribute("time");;
        session.removeAttribute("questionList");;
        return model;
    }

    @GetMapping("/quiz_list/{sectionId}")
    public ModelAndView ViewPage(HttpServletRequest request, @PathVariable("sectionId") Long sectionId) {
        String msg = request.getParameter("msg");
        ModelAndView model = new ModelAndView();
        model.addObject("sectionId",sectionId);
        model.setViewName("page/QuizManager");
        if (msg != null && !msg.equals("")) {
            model.addObject("msg", msg);
        }
        model.addObject("quizList",quizService.getQuizsBySectionId(sectionId));
        return model;
    }
}
