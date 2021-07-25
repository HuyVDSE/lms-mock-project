package com.app.controller;

import com.app.model.*;
import com.app.repository.QuestionForQuizRepo;
import com.app.service.AnswerService;
import com.app.service.MarkService;
import com.app.service.QuestionForQuizService;
import com.app.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/page/history/detail")
public class HistoryDetailController {

    @Autowired
    private MarkService markService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private QuestionForQuizService questionForQuizService;

    @Autowired
    private QuestionForQuizRepo questionForQuizRepo;

    @Autowired
    private AnswerService answerService;

    @GetMapping("/{markId}")
    public ModelAndView HistoryDetail(@PathVariable("markId") int markId) {
        Mark mark = markService.getMarkByMarkId(markId);
        List<Result> resultList = resultService.findAllByMarkId(markId);
        List<QuestionForQuiz> questionForQuizList = new ArrayList<>();
        for (Result result : resultList) {
            questionForQuizList.add(result.getQuestionForQuiz());
        }
        List<Integer> correctAnswer = new ArrayList<>();
//        for (int i = 0; i < resultList.size(); i++) {
//            String correct = null;
//            for(Answer answer : questionForQuizList.get(i).getQuestion().getAnswerList()){
//                if(answer.isStatus()) {
//                    correct = answer.getAnswer();
//                }
//            }
//            if(resultList.get(i).getAnswer().equals(correct)) {
//                correctAnswer.add(questionForQuizList.get(i).getId());
//            }
//
//        }
        List<Question> questionList = new ArrayList<>();
        for (QuestionForQuiz question: questionForQuizList) {
            questionList.add(question.getQuestion());
        }
        ModelAndView model = new ModelAndView();
        model.addObject("mark", mark.getMark());
        model.addObject("questionList", questionList);
        model.addObject("resultList", resultList);
        model.setViewName("page/detal_quiz");
        return model;
    }
}
