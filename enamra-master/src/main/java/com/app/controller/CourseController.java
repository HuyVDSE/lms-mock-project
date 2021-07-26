package com.app.controller;


import com.app.model.Course;
import com.app.model.Section;
import com.app.model.Topic;
import com.app.repository.SectionRepository;
import com.app.repository.TopicRepository;
import com.app.service.CourseImageService;
import com.app.service.impl.CourseService;
import com.app.service.impl.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/v/course")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private TopicRepository topicRepository;

    @GetMapping("/full/{course_id}")
    public ModelAndView fullCourse(@PathVariable("course_id") Long id) {
        ModelAndView model = new ModelAndView("page/course_full");
        Course course = courseService.findCourseById(id);
        List<Section> allSection = sectionRepository.loadSectionByCourseId(course.getCourse_id());
        model.addObject("course_full", course);
        model.addObject("section", allSection);
        return model;
    }


    @GetMapping("/all_topic/{section_id}")
    public ModelAndView allTopic(@PathVariable("section_id") Long id) {
        ModelAndView model = new ModelAndView("page/full_topic");
        Section findSection = sectionService.findSectionByID(id);
        Course findCourse = courseService.findCourseById(findSection.getCourse().getCourse_id());
        List<Topic> topicList = topicRepository.findAllTopicBySectionId(findSection.getSection_id());

        model.addObject("course1", findCourse);
        model.addObject("section1", findSection);
        model.addObject("topicList1", topicList);
        model.addObject("sectionId", id);

        return model;
    }

    @Value("${course.file.dir}")
    private String courseImageDir;

    @GetMapping("/load-image/{course_id}")
    public void loadCourseImage(@PathVariable("course_id") Long id,
                                HttpServletResponse response) throws IOException {
        Course course = courseService.findCourseById(id);
        File imageFile = new File(courseImageDir + "/" + course.getImageUrl());
        response.setHeader("Content-Length", String.valueOf(imageFile.length()));
        response.setHeader("Content-Disposition", "inline-filename\"" + imageFile.getName() + "\"");
        Files.copy(imageFile.toPath(), response.getOutputStream());
    }
}
