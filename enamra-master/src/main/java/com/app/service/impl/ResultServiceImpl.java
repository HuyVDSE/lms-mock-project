package com.app.service.impl;

import com.app.model.Result;
import com.app.repository.ResultRepo;
import com.app.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepo resultRepo;

    @Override
    public void saveResult(Result result) {
        resultRepo.save(result);
    }

    @Override
    public List<Result> findAllByMarkId(int markId) {
        return resultRepo.findAllByMarkId(markId);
    }
}
