package com.app.service.impl;

import com.app.model.Quiz;
import com.app.repository.QuizRepo;
import com.app.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepo quizRepo;

    @Override
    public void saveQuiz(Quiz quiz) {
        quizRepo.save(quiz);
    }

    @Override
    public boolean findByQuestion(String question) {
        Quiz quiz = quizRepo.findByQuestion(question);
        try{
            if(quiz.getQuestion() != null) {
                return true;
            }
        } catch (Exception e){
            return false;
        }
        return false;
    }
}
