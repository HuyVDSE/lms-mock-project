package com.app.repository;

import com.app.model.CourseFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseFileRepository extends JpaRepository<CourseFile, Integer> {

    @Query(value = "select * from course_file where course_id = ? order by file_id desc limit 1", nativeQuery = true)
    CourseFile getCourseFileByCourse(Long courseId);
}
