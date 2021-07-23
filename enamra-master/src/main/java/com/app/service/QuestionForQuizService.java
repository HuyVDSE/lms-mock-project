package com.app.service;

import com.app.model.QuestionForQuiz;

public interface QuestionForQuizService {
    void saveQuestionForQuiz(QuestionForQuiz questionForQuiz);
    int getLastID();
}
