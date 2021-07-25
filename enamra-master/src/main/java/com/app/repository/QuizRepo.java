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

    @Query(value = "SELECT q FROM Quiz q WHERE q.user.id = ?1")
    List<Quiz> findAllByByUserId(Long userId);

    Quiz findQuizByQuizId(int quizId);

}
