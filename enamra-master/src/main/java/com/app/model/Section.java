package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long section_id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Topic> topicList= new ArrayList<>();

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Question> questionList;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Quiz> quizList;

    public void addQuiz(Quiz quiz) {
        if (quizList == null) {
            quizList = new ArrayList<>();
        }

        quizList.add(quiz);
        quiz.setSection(this);
    }

    @Override
    public String toString() {
        return "Section{" +
                "section_id=" + section_id +
                ", name='" + name + '\'' +
                '}';
    }
}
