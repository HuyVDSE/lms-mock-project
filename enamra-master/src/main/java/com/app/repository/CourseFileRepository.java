package com.app.repository;

import com.app.model.CourseFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseFileRepository extends JpaRepository<CourseFile, Integer> {
    CourseFile getCourseFileByCourse(Long courseId);
}
