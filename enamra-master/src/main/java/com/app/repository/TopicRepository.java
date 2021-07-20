package com.app.repository;

import com.app.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Long> {

    @Query(value = "select * from topic t where t.section_id=? ", nativeQuery = true)
    List<Topic> findAllTopicBySectionId(Long id);
}
