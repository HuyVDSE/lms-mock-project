package com.app.repository;

import com.app.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("questionRepo")
public interface QuestionRepo extends JpaRepository<Question,Integer> {
    @Query(value = "select * from question where question=?", nativeQuery = true)
    Question findByQuestion(String question);

    @Query(value = "select * from question where questionid=?", nativeQuery = true)
    Question findByQuestionID(int questionId);

    @Query(value = "SELECT NUM " +
                   "FROM(SELECT FLOOR(RAND() * (10000)) AS 'NUM') as SUBQ " +
                   "WHERE 'NUM' NOT IN(SELECT questionid FROM `question`)", nativeQuery = true)
    int getLastID();

    @Query(value = "SELECT q FROM Question q WHERE q.section.section_id = ?1")
    List<Question> findBySectionId(Long sectionId);

    @Query(value = "SELECT q FROM Question q WHERE q.section.section_id = ?1 and q.question like %?2%")
    Page<Question> findAllBySectionId(Pageable pageable, Long sectionId, String search_content);

    @Query(value = "SELECT q FROM Question q WHERE q.section.section_id = ?1 and q.question like %?2%")
    List<Question> searchQuestion(Long sectionId, String search_content);

    @Query(value = "SELECT * FROM Question q WHERE  section_id = ?1 and status Like 'Active' ORDER BY RAND() LIMIT ?2",nativeQuery = true)
    List<Question> getRandomQuestion(Long sectionId, Integer NumOfRandom);

    @Query(value = "SELECT q FROM Question q WHERE q.section.section_id = ?1 and q.status like 'Active'")
    List<Question> findBySectionIdActive(Long sectionId);
}
