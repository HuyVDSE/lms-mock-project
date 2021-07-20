package com.app.repository;

import com.app.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("questionRepo")
public interface QuestionRepo extends JpaRepository<Question,Integer> {
    @Query(value = "select * from Question where question=?", nativeQuery = true)
    Question findByQuestion(String question);
}
