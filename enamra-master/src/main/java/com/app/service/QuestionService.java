package com.app.service;

import com.app.model.Question;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuestionService {
    void saveQuestion(Question question);
    boolean findByQuestion(String question);
    Question findById(Integer questionId);
    boolean checkById(Integer questionId);
    int getLastID();
    void deleteQuestion(int questionId);
    List<Question> searchQuestion(Long sectionId, String searchContent);
    List<Question> getQuestionsBySectionId(Long sectionId);
    Page<Question> findPaginatedBySection(int pageNo, int pageSize, Long sectionId, String search_content);
    List<Question> getRanDomQuestion(Long sectionId, Integer NumOfQues);
    List<Question> getQuestionsBySectionIdActive(Long sectionId);
}
