package com.app.controller.admin;


import com.app.model.Course;
import com.app.model.Section;
import com.app.repository.CourseRepository;
import com.app.repository.SectionRepository;
import com.app.service.CourseImageService;
import com.app.service.impl.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/course")
public class CourseManagerController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private SectionRepository sectionRepository;

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
        ModelAndView model =  new ModelAndView("redirect:/admin/course/last_10_course");
        return model;
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
            model.addObject("msg", "Course Update successfully");
            model.setViewName("admin/update_course");
        }
        return model;
    }


    @GetMapping("/last_10_course")
    public ModelAndView showAllCourses(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("admin/course_list_last_10");
        List<Course> courseList = courseService.getAllCourses();
        model.addObject("courseLists", courseList);
        String msg = request.getParameter("msg");

        if (msg != null && !msg.equals("")) {
            model.addObject("msg", msg);
        }
        return model;
    }

    @GetMapping("/course_with_section_last_10")
    public ModelAndView showCourseWithSection() {
        ModelAndView model = new ModelAndView();
        List<Course> courseList = courseService.getCourseWithSection();
        model.addObject("course_w_sec", courseList);
        model.setViewName("admin/course_with_section");
        return model;
    }

    @GetMapping("/single_course_with_all_section/{course_id}")
    public String singleCourseWithAllSection(@PathVariable("course_id") Long id, Model model) {
        Course courseFound = courseService.findCourseById(id);
        List<Section> sections = sectionRepository.loadSectionByCourseId(id);
        model.addAttribute("course", courseFound);
        model.addAttribute("all_sec", sections);
        return "admin/single_course_with_all_sec";
    }

    @GetMapping("/last_10_final")
    public ModelAndView finalCourse() {
        ModelAndView model = new ModelAndView("admin/last_10_final");
        List<Course> courseList = courseService.getAllCourses();
        model.addObject("courseLists", courseList);
        return model;
    }

    @GetMapping("/full_details/{course_id}")
    public ModelAndView loadFullDetailOfCourse(@PathVariable("course_id") Long id) {
        ModelAndView model = new ModelAndView("admin/full_course");
        Course findCourse = courseService.findCourseById(id);
        List<Section> allSection = sectionRepository.loadSectionByCourseId(findCourse.getCourse_id());
        model.addObject("course", findCourse);
        model.addObject("allSection", allSection);
        return model;
    }

    @GetMapping("/blog/{course_id}")
    public ModelAndView loadBlogPage(@PathVariable("course_id") Long id) {
        ModelAndView model = new ModelAndView("admin/blog");
        Course findCourse = courseService.findCourseById(id);
        model.addObject("course", findCourse);

        return model;
    }
}
