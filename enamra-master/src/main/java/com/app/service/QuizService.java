package com.app.service;

import com.app.model.Question;
import com.app.model.Quiz;

import java.util.List;

public interface QuizService {
    List<Quiz> getQuizsBySectionId(Long sectionId);
    void saveQuiz(Quiz quiz);
    Quiz getQuizByQuizId(int quizId);
}
