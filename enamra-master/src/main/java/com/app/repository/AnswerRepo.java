package com.app.repository;

import com.app.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("answerRepo")
public interface AnswerRepo  extends JpaRepository<Answer,Integer> {
}
