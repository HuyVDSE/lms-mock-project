package com.app.repository;

import com.app.model.Quiz;
import com.app.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("resultRepo")
public interface ResultRepo extends JpaRepository<Result, Integer> {
}
