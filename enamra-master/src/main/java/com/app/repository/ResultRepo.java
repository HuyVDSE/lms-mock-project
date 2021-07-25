package com.app.repository;

import com.app.model.Mark;
import com.app.model.Quiz;
import com.app.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("resultRepo")
public interface ResultRepo extends JpaRepository<Result, Integer> {
    @Query(value = "SELECT r FROM Result r WHERE r.mark.markId = ?1")
    List<Result> findAllByMarkId(int markId);
}
