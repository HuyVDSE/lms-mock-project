package com.app.service;

import com.app.model.Quiz;
import com.app.model.User;

public interface QuizService {
    void saveQuiz(Quiz quiz);
    boolean findByQuestion(String question);
}
