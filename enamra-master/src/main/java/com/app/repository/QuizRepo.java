package com.app.repository;

import com.app.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("quizRepo")
public interface QuizRepo extends JpaRepository<Quiz,Integer> {
    @Query(value = "select * from quiz where question=?", nativeQuery = true)
    Quiz findByQuestion(String question);
}
