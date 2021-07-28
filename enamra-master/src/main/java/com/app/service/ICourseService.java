package com.app.service;

import com.app.model.Course;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ICourseService {

    List<Course> findAllCourse();

    Course findCourseById(Long id);

    void saveCourse(Course course);

    void deleteCourse(Long id);

    List<Course> getAllCourses();

    List<Course> getCourseWithSection();
}
