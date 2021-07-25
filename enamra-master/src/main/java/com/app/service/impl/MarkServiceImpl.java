package com.app.service.impl;

import com.app.model.Mark;
import com.app.repository.MarkRepo;
import com.app.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MarkServiceImpl implements MarkService {

    @Autowired
    private MarkRepo markRepo;

    @Override
    public void save(Mark mark) {
        markRepo.save(mark);
    }

    @Override
    public List<Mark> getMarksByUserId(Long userId) {
        return markRepo.findAllByByUserId(userId);
    }

    @Override
    public List<Mark> getMarksByQuizId(int quizId) {
        return markRepo.findAllByQuizId(quizId);
    }

    @Override
    public Mark getMarkByMarkId(int markId) {
        return markRepo.findMarkByMarkId(markId);
    }
}
