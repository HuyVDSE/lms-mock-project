package com.app.service;

import com.app.model.Question;

public interface QuestionService {
    void saveQuestion(Question question);
    boolean findByQuestion(String question);
    Question findById(Integer questionId);
    int getLastID();
}
