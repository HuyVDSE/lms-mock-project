package com.app.model;


import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "section_id")
@Entity(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic_name;

    private String topic_description;

    private String video_name;

    private String video_path;

    private String topic_readingPDF;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.REMOVE)
    private Collection<Comments> comments;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

}
