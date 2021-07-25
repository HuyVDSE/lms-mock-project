package com.app.service;

import com.app.model.Mark;

import java.util.List;

public interface MarkService {
    void save(Mark mark);
    List<Mark> getMarksByUserId(Long userId);
    List<Mark> getMarksByQuizId(int quizId);
    Mark getMarkByMarkId(int markId);
}
