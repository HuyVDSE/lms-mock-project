package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "Question")
public class Question {
    @Id
    private int questionID;
    private String question;
    private Date createDate;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answerList;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionForQuiz> questionForQuizList;

    public void addAnswer(Answer answer) {
        if (answerList == null) {
            answerList = new ArrayList<>();
        }
        answerList.add(answer);
        answer.setQuestion(this);
    }
}
