package com.app.service.impl;

import com.app.model.Course;
import com.app.repository.CourseRepository;
import com.app.service.CourseImageService;
import com.app.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class CourseService implements ICourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> findAllCourse() {
        return courseRepository.findAll();
    }

    @Override
    public Course findCourseById(Long id) {
        return courseRepository.findById(id).get();
    }

    @Override
    public void saveCourse(Course course) {
        course.setActive(true);
        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getCourseWithSection() {
        return courseRepository.findAllCourseWithSection();
    }
}
