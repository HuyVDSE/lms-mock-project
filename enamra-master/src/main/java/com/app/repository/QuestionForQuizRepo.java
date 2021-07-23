package com.app.repository;

import com.app.model.QuestionForQuiz;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("questionForQuizRepo")
public interface QuestionForQuizRepo extends JpaRepository<QuestionForQuiz, Integer> {
    @Query(value = "SELECT NUM " +
            "FROM(SELECT FLOOR(RAND() * (10000)) AS 'NUM') as SUBQ " +
            "WHERE 'NUM' NOT IN(SELECT id FROM `question_for_quiz`)", nativeQuery = true)
    int getLastID();
}