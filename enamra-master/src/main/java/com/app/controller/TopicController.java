package com.app.controller;


import com.app.model.Comments;
import com.app.model.Course;
import com.app.model.Section;
import com.app.model.Topic;
import com.app.repository.CommentRepository;
import com.app.repository.TopicRepository;
import com.app.service.impl.CourseService;
import com.app.service.impl.SectionService;
import com.app.service.impl.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/g/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CommentRepository commentRepository;

    /// single_topic

    @GetMapping("/single_topic/{id}")
    public String singleTopic(@PathVariable("id") Long id, Model model) {
        Topic topic = topicService.findTopicByID(id);
        Section section = sectionService.findSectionByID(topic.getSection().getSection_id());
        Course course = courseService.findCourseById(section.getCourse().getCourse_id());

        List<Comments> commentsList = commentRepository.findAll_by_desc(id);
        List<Topic> topicList = topicRepository.all_topic_BY_Section_ID(section.getSection_id());

        model.addAttribute("course", course);
        model.addAttribute("section", section);
        model.addAttribute("topic_single", topic);
        model.addAttribute("topicList", topicList);
        model.addAttribute("commentsList", commentsList);
        return "page/single_topic";
    }
}
