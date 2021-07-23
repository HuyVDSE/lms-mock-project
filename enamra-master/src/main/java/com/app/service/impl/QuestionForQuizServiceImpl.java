package com.app.service.impl;

import com.app.model.QuestionForQuiz;
import com.app.repository.QuestionForQuizRepo;
import com.app.service.QuestionForQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionForQuizServiceImpl implements QuestionForQuizService {

    @Autowired
    private QuestionForQuizRepo questionForQuizRepo;
    @Override
    public void saveQuestionForQuiz(QuestionForQuiz questionForQuiz) {
        questionForQuizRepo.save(questionForQuiz);
    }
    @Override
    public int getLastID() {
        try {
            return questionForQuizRepo.getLastID();
        } catch (Exception e) {
            return -1;
        }
    }
}
