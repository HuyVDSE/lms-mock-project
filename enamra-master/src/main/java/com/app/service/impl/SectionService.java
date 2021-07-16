package com.app.service.impl;

import com.app.model.Course;
import com.app.model.Section;
import com.app.repository.CourseRepository;
import com.app.repository.SectionRepository;
import com.app.service.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SectionService implements ISectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Section> getAllSection() {
        return sectionRepository.findAll();
    }

    @Override
    public Section findSectionByID(Long id) {
        return sectionRepository.findById(id).get();
    }

    @Override
    public void saveSection(Section section, Long section_id) {
        Course courseFound = courseRepository.findById(section_id).get();
        section.setCourse(courseFound);
        sectionRepository.save(section);
    }

    @Override
    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }
}
