package com.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "course_file")
@Data
@NoArgsConstructor
public class Course_files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private String file_id;

    @Column(name = "file_name")
    private String file_name;

    @Column(name = "file_dir")
    private String file_dir;

    @Column(name = "file_path")
    private String file_path;

    @Column(name = "file_extension")
    private String file_extension;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;
}
