package com.app.service;

import com.app.model.Question;

public interface QuizService {
    void saveQuiz(Question question);
    boolean findByQuestion(String question);
}
