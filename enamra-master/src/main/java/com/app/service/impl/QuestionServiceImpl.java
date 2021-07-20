package com.app.service.impl;

import com.app.model.Question;
import com.app.repository.QuestionRepo;
import com.app.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepo questionRepo;

    @Override
    public void saveQuestion(Question question) {
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

    @Override
    public Question findById(Integer questionId) {
        Optional<Question> optional = questionRepo.findById(questionId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public int getLastID() {
        try {
            return questionRepo.getLastID();
        } catch (Exception e) {
            return -1;
        }
    }
}
