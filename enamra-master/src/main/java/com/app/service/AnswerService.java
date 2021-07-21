package com.app.service;

import com.app.model.Answer;

import java.util.List;

public interface AnswerService {
    void saveAnswer(Answer answer);
    List<Answer> getAnswersByQuestionId(int questionId);
}
