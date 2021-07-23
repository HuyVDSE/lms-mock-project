package com.app.service.impl;

import com.app.model.Question;
import com.app.model.Quiz;
import com.app.repository.QuizRepo;
import com.app.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepo quizRepo;
    @Override
    public List<Quiz> getQuizsBySectionId(Long sectionId) {
        return quizRepo.findAllBySectionId(sectionId);
    }



    @Override
    public void saveQuiz(Quiz quiz) {
        quizRepo.save(quiz);
    }
    @Override
    public int getLastID() {
        try {
            return quizRepo.getLastID();
        } catch (Exception e) {
            return -1;
        }
    }
}
