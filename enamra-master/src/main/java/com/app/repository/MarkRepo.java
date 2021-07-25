package com.app.repository;

import com.app.model.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarkRepo extends JpaRepository<Mark,Long> {

    @Query(value = "SELECT m FROM Mark m WHERE m.user.id = ?1")
    List<Mark> findAllByByUserId(Long userId);

    @Query(value = "SELECT m FROM Mark m WHERE m.quiz.quizId = ?1")
    List<Mark> findAllByQuizId(int quizId);

    @Query(value = "SELECT * FROM mark WHERE mark_id = ?", nativeQuery = true)
    Mark findMarkByMarkId(int markId);
}
