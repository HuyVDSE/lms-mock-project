package com.app.repository;

import com.app.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("questionRepo")
public interface QuestionRepo extends JpaRepository<Question,Integer> {
    @Query(value = "select * from question where question=?", nativeQuery = true)
    Question findByQuestion(String question);

    @Query(value = "SELECT NUM " +
                   "FROM(SELECT FLOOR(RAND() * (10000)) AS 'NUM') as SUBQ " +
                   "WHERE 'NUM' NOT IN(SELECT questionid FROM `question`)", nativeQuery = true)
    int getLastID();

    @Query(value = "SELECT q FROM Question q WHERE q.section.section_id = ?1")
    List<Question> findBySectionId(Long sectionId);


}
