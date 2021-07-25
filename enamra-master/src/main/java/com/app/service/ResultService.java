package com.app.service;


import com.app.model.Result;

import java.util.List;

public interface ResultService {
    void saveResult(Result result);
    List<Result> findAllByMarkId(int markId);
}
