package com.app.repository;

import com.app.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    @Query(value = "select * from course c where c.visibility = 'SHOW' order by course_id desc limit 0,4", nativeQuery = true)
    List<Course> recently_added_first_four_course();

    @Query(value = "select * from course c where c.visibility = 'SHOW' order by course_id desc limit 4,4", nativeQuery = true)
    List<Course> recently_added_second_four_course();

    @Query(value = "SELECT c FROM course c where c.course_id in (select s.course.course_id from section s)")
    List<Course> findAllCourseWithSection();
}
