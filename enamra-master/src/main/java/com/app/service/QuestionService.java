package com.app.service;

import com.app.model.Question;

import java.util.List;

public interface QuestionService {
    void saveQuestion(Question question);
    boolean findByQuestion(String question);
    Question findById(Integer questionId);
    int getLastID();

    List<Question> getQuestionsBySectionId(Long sectionId);
}
