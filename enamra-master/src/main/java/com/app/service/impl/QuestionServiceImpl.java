package com.app.service.impl;

import com.app.model.Question;
import com.app.repository.QuestionRepo;
import com.app.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepo questionRepo;

    @Override
    public void saveQuestion(Question question) {
        questionRepo.save(question);
    }

    @Override
    public boolean findByQuestion(String question) {
        Question quiz = questionRepo.findByQuestion(question);
        try{
            if(quiz.getQuestion() != null) {
                return true;
            }
        } catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public Question findById(Integer questionId) {
        Optional<Question> optional = questionRepo.findById(questionId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public boolean checkById(Integer questionId) {
        Question quiz = questionRepo.findByQuestionID(questionId);
        try{
            if(quiz.getQuestion() != null) {
                return true;
            }
        } catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public int getLastID() {
        try {
            return questionRepo.getLastID();
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void deleteQuestion(int questionId) {
        Question question = questionRepo.getOne(questionId);
        question.setStatus("Inactive");
        questionRepo.save(question);
    }

    @Override
    public List<Question> getQuestionsBySectionId(Long sectionId) {
        return questionRepo.findBySectionId(sectionId);
    }

    @Override
    public Page<Question> findPaginatedBySection(int pageNo, int pageSize, Long sectionId, String search_content) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return questionRepo.findAllBySectionId(pageable, sectionId, search_content);
    }

    @Override
    public List<Question> searchQuestion(Long sectionId, String searchContent) {
        return questionRepo.searchQuestion(sectionId,searchContent);
    }

    @Override
    public List<Question> getRanDomQuestion(Long sectionId, Integer NumOfQues) {
        return questionRepo.getRandomQuestion(sectionId,NumOfQues);
    }

    @Override
    public List<Question> getQuestionsBySectionIdActive(Long sectionId) {
        return questionRepo.findBySectionIdActive(sectionId);
    }
}
