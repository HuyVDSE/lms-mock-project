package com.app.controller;

import com.app.EnamraApplication;
import com.app.model.Section;
import com.app.repository.SectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnamraApplication.class)
public class SectionControllerTest {


    @Autowired
    private SectionRepository sectionRepository;


    @Test
    public void single_course_with_all_section() {
        List<Section> list = sectionRepository.loadSectionByCourseId(11l);
        log.info("Section For Single Course -> {}", list);
    }


   /* @Test
    public void section_with_last_10_course_desc() {
        List<Section> sectionList = sectionRepo.sec_w_last_10_cr();
        log.info("PPxxxxx -> {}",sectionList);
    }*/
}