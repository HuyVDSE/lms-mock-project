package com.app.controller;


import com.app.model.Course;
import com.app.model.Section;
import com.app.repository.CourseRepository;
import com.app.repository.SectionRepository;
import com.app.repository.TopicRepository;
import com.app.service.CourseImageService;
import com.app.service.impl.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/course")
public class CourseController_For_ADMIN {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CourseImageService courseImageService;

    @GetMapping
    public String coursePage() {
        return "admin/coursePage";
    }

    @GetMapping("/create")
    public ModelAndView loadCourseForm() {
        ModelAndView model = new ModelAndView("admin/courseForm");
        model.addObject("course", new Course());
        return model;
    }

    @PostMapping("/create")
    public ModelAndView createCourse(@Valid Course course, MultipartFile file,
                                     BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();

        if (bindingResult.hasErrors()) {
            model.setViewName("admin/courseForm");
            model.addObject("error", "Something Went Wrong ......");
        } else {
            if (file.getOriginalFilename().equals("")) {
                course.setImageUrl("courses.png");
            } else {
                course.setImageUrl(file.getOriginalFilename());
                courseImageService.saveImage(file);
            }
            courseService.saveCourse(course);
            model.addObject("msg", "Course created successfully");
            model.setViewName("admin/courseForm");
        }
        return model;
    }

    @GetMapping("/delete_course/{course_id}")
    public ModelAndView deleteCourse(@PathVariable("course_id") Long id) {
        courseService.deleteCourse(id);
        return new ModelAndView("redirect:/admin/course/last_10_course");
    }


    @GetMapping("/update_course/{course_id}")
    public ModelAndView updateCourse(@PathVariable("course_id") Long id) {
        ModelAndView model = new ModelAndView("admin/update_course");
        Course findCourse = courseService.findCourseById(id);
        model.addObject("course", findCourse);
        return model;
    }

    @PostMapping("/update")
    public ModelAndView saveUpdateCourse(@Valid Course course, MultipartFile file,
                                         BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        if (bindingResult.hasErrors()) {
            model.addObject("error", "something going wrong");
        } else {
            Course oldCourse = courseService.findCourseById(course.getCourse_id());
            String imageUrlOfCourse = oldCourse.getImageUrl();

            if (file.getOriginalFilename().equals("")) {
                course.setImageUrl(imageUrlOfCourse);
            } else {
                course.setImageUrl(file.getOriginalFilename());
                courseImageService.saveImage(file);
            }

            courseService.saveCourse(course);
            model.addObject("msg", "Course created successfully");
            model.setViewName("admin/update_course");
        }
        return model;
    }


    @GetMapping("/last_10_course")
    public ModelAndView show_Last_10_course() {
        ModelAndView model = new ModelAndView("admin/course_list_last_10");
        List<Course> courseList = courseService.getLast10Course();
        model.addObject("courseLists", courseList);
        return model;
    }

    @GetMapping("/course_with_section_last_10")
    public ModelAndView course_with_section() {
        ModelAndView model = new ModelAndView();
        List<Course> courseList = courseRepository.findAll();
        model.addObject("course_w_sec", courseList);
        model.setViewName("admin/course_with_section");
        return model;
    }

    @GetMapping("/single_course_with_all_section/{course_id}")
    public ModelAndView single_course_wit_all_section(@PathVariable("course_id") Long id) {
        ModelAndView model = new ModelAndView("admin/single_course_with_all_sec");
        Course courseFound = courseService.findCourseById(id);
        List<Section> allSectionByCourseId = sectionRepository.loadSectionByCourseId(courseFound.getCourse_id());
        model.addObject("course", courseFound);
        model.addObject("all_sec", allSectionByCourseId);
        return model;
    }

    // last_10_final
    @GetMapping("/last_10_final")
    public ModelAndView finalCourse() {
        ModelAndView model = new ModelAndView("admin/last_10_final");
        List<Course> courseList = courseService.getLast10Course();
        model.addObject("courseLists", courseList);
        return model;
    }

    //admin/course/full_details
    @GetMapping("/full_details/{course_id}")
    public ModelAndView loadFullDetailOfCourse(@PathVariable("course_id") Long id) {
        ModelAndView model = new ModelAndView("admin/full_course");
        Course findCourse = courseService.findCourseById(id);
        List<Section> allSection = sectionRepository.loadSectionByCourseId(findCourse.getCourse_id());
        model.addObject("course", findCourse);
        model.addObject("allSection", allSection);
        return model;
    }

    // /admin/course/blog/__${course_id}__
    @GetMapping("/blog/{course_id}")
    public ModelAndView loadBlogPage(@PathVariable("course_id") Long id) {
        ModelAndView model = new ModelAndView("admin/blog");
        Course findCourse = courseService.findCourseById(id);
        model.addObject("course", findCourse);

        return model;
    }

}
