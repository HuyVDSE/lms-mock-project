package com.app.service.impl;

import com.app.model.Answer;
import com.app.repository.AnswerRepo;
import com.app.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerRepo answerRepo;

    @Override
    public void saveAnswer(Answer answer) {
        answerRepo.save(answer);
    }
}
