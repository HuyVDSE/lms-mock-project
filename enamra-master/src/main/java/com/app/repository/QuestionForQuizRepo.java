package com.app.repository;

import com.app.model.QuestionForQuiz;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("questionForQuizRepo")
public interface QuestionForQuizRepo extends JpaRepository<QuestionForQuiz, Integer> {
    @Query(value = "SELECT NUM " +
            "FROM(SELECT FLOOR(RAND() * (10000)) AS 'NUM') as SUBQ " +
            "WHERE 'NUM' NOT IN(SELECT id FROM `question_for_quiz`)", nativeQuery = true)
    int getLastID();

    @Query(value = "SELECT * FROM question_for_quiz  WHERE quiz_id = ?", nativeQuery = true)
    List<QuestionForQuiz> findAllByQuizId(Integer QuizId);

    @Query(value = "SELECT q FROM QuestionForQuiz q WHERE q.question.questionID = ?1 and q.quiz.quizId=?2")
    QuestionForQuiz findByQuestionId(Integer QuestionId, Integer QuizId);
}