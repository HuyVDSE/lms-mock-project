package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
    private String subjectID;
}
