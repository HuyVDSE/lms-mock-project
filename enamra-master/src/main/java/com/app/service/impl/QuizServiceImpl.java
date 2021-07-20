package com.app.service.impl;

import com.app.model.Question;
import com.app.repository.QuestionRepo;
import com.app.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuestionRepo questionRepo;

    @Override
    public void saveQuiz(Question question) {
        questionRepo.save(question);
    }

    @Override
    public boolean findByQuestion(String question) {
        Question quiz = questionRepo.findByQuestion(question);
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
