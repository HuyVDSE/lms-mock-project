package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "Quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int quizId;
    private String name;
    private int totalTime;
    private Timestamp startDate;
    private Timestamp endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuestionForQuiz> questionForQuizList;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Mark> markList;
}
