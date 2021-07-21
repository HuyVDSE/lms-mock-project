package com.app.repository;

import com.app.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("answerRepo")
public interface AnswerRepo  extends JpaRepository<Answer,Integer> {

    @Query(value = "SELECT a FROM Answer a WHERE a.question.questionID = ?1")
    List<Answer> findAnswersByQuestion(int questionId);
}
