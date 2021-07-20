package com.app.controller.admin;


import com.app.model.Course;
import com.app.model.Section;
import com.app.repository.SectionRepository;
import com.app.service.impl.CourseService;
import com.app.service.impl.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/sec")
public class SectionManagerController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionRepository sectionRepository;

    @GetMapping("/addSec_for_course/{courseId}")
    public String loadSectionForm(@PathVariable("courseId") Long courseId, Model model) {
        model.addAttribute("course", courseId);
        model.addAttribute("section", new Section());
        return "admin/SectionForm";
    }

    @PostMapping("/addSec_for_course")
    public ModelAndView saveSection(@Valid @ModelAttribute("section") Section section,
                                    BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();

        if (bindingResult.hasErrors()) {
            model.addObject("error", "something went wrong .........");
            model.setViewName("admin/SectionForm");
        } else {
            Long courseId = section.getCourse().getCourse_id();
            sectionService.saveSection(section, courseId);
            model.addObject("course", courseId);
            model.addObject("msg", "Section/chapter created successfully...");
            model.setViewName("admin/SectionForm");
        }
        return model;
    }

    @GetMapping("/delete_sec/{section_id}")
    public String deleteSection(@PathVariable("section_id") Long id){
        Long courseId = sectionService.findSectionByID(id).getCourse().getCourse_id();
        sectionService.deleteSection(id);
        return "redirect:/admin/course/single_course_with_all_section/" + courseId;
    }

    @GetMapping("/update_sec/{section_id}")
    public ModelAndView loadUpdateSectionForm(@PathVariable("section_id") Long id){
        ModelAndView model = new ModelAndView();
        Section find_section = sectionService.findSectionByID(id);
        model.addObject("section", find_section);
        model.setViewName("admin/UpdateSection");
        return model;

    }

    @PostMapping("/update_sec")
    public ModelAndView saveUpdateSection(@Valid Section section, BindingResult bindingResult){
        ModelAndView model = new ModelAndView();
        if (bindingResult.hasErrors()){
            model.addObject("error","something went wrong ....");
        }else {
            sectionRepository.save(section);
            model.addObject("msg","Course Has been Updated Successfully...");
            model.setViewName("admin/UpdateSection");
        }
        return model;
    }

}
