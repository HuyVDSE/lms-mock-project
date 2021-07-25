package com.app.service.impl;

import com.app.model.QuestionForQuiz;
import com.app.repository.QuestionForQuizRepo;
import com.app.service.QuestionForQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionForQuizServiceImpl implements QuestionForQuizService {

    @Autowired
    private QuestionForQuizRepo questionForQuizRepo;
    @Override
    public void saveQuestionForQuiz(QuestionForQuiz questionForQuiz) {
        questionForQuizRepo.save(questionForQuiz);
    }
    @Override
    public int getLastID() {
        try {
            return questionForQuizRepo.getLastID();
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void deleteQuestionForQuiz(QuestionForQuiz questionForQuiz) {
        questionForQuizRepo.delete(questionForQuiz);
    }

    @Override
    public List<QuestionForQuiz> getAllByQuizId(Integer quizId) {
        return questionForQuizRepo.findAllByQuizId(quizId);
    }

    @Override
    public void deleteQuestionInQuiz(int questionId,int quizId) {
        QuestionForQuiz questionForQuiz = questionForQuizRepo.findByQuestionId(questionId, quizId);
        questionForQuizRepo.delete(questionForQuiz);
    }
}
