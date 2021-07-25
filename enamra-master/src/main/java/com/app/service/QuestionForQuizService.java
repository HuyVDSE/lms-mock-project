package com.app.service;

import com.app.model.QuestionForQuiz;

import java.util.List;

public interface QuestionForQuizService {
    void saveQuestionForQuiz(QuestionForQuiz questionForQuiz);
    int getLastID();
    void deleteQuestionForQuiz(QuestionForQuiz questionForQuiz);
    List<QuestionForQuiz> getAllByQuizId(Integer quizId);
    void deleteQuestionInQuiz(int questionId,int quizId);
}
