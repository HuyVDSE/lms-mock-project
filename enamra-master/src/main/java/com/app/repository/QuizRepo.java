package com.app.repository;

import com.app.model.Question;
import com.app.model.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("quizRepo")
public interface QuizRepo extends JpaRepository<Quiz, Integer> {
    @Query(value = "SELECT q FROM Quiz q WHERE q.section.section_id = ?1")
    List<Quiz> findAllBySectionId(Long sectionId);
    @Query(value = "SELECT NUM " +
            "FROM(SELECT FLOOR(RAND() * (10000)) AS 'NUM') as SUBQ " +
            "WHERE 'NUM' NOT IN(SELECT quiz_id FROM `quiz`)", nativeQuery = true)
    int getLastID();

}
