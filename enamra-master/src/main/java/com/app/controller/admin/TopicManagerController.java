package com.app.controller.admin;


import com.app.model.Comments;
import com.app.model.Course;
import com.app.model.Section;
import com.app.model.Topic;
import com.app.repository.CommentRepository;
import com.app.repository.TopicRepository;
import com.app.service.ICourseService;
import com.app.service.ISectionService;
import com.app.service.ITopicService;
import com.app.service.TopicPDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/topic")
public class TopicManagerController {

    @Autowired
    private ITopicService topicService;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ISectionService sectionService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TopicPDFService topicPDFService;


    @GetMapping("/addTopic_for_section/{sectionId}")
    public String addTopicPage(@PathVariable("sectionId") Long sectionId, Model model) {
        model.addAttribute("sectionId", sectionId);
        model.addAttribute("topic", new Topic());
        return "admin/topicForm";
    }

    @PostMapping("/addTopic_for_section")
    public String saveNewTopic(@Valid Topic topic, MultipartFile file, BindingResult bindingResult, Model model) {
        String youtubeLinkRegex = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";

        if (!topic.getVideo_path().matches(youtubeLinkRegex)) {
            bindingResult.rejectValue("video_path", "error.topic",
                    "Invalid youtube link!!");
        }

        model.addAttribute("sectionId", topic.getSection().getSection_id());

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Something Went Wrong");
            return "admin/topicForm";
        } else {
            if (file.getOriginalFilename().equals("")) {
                topic.setTopic_readingPDF("None");
            } else {
                topic.setTopic_readingPDF(file.getOriginalFilename());
                topicPDFService.savePDF(file);
            }

            topicService.saveTopic(topic);
            model.addAttribute("msg", " Topic Created Successfully");
        }
        return "admin/topicForm";
    }


    // /admin/topic/see_all_topic_for_this_section/{section_id}
    @GetMapping("/see_all_topic_for_this_section/{section_id}")
    public ModelAndView see_topic_for_section(@PathVariable("section_id") Long id) {
        ModelAndView model = new ModelAndView();
        Section findSection = sectionService.findSectionByID(id);
        Course findCourseForSection = courseService.findCourseById(findSection.getCourse().getCourse_id());
        List<Topic> topicList = topicRepository.findAllTopicBySectionId(id);  //topicService.getAllTopic();
        model.addObject("course", findCourseForSection);
        model.addObject("section", findSection);
        model.addObject("topicList", topicList);
        model.setViewName("admin/single_section_with_all_topic");
        return model;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteTopic(@PathVariable("id") Long id) {
        topicService.deleteTopicByID(id);
        return new ModelAndView("redirect:/admin/course");
    }

    // /admin/topic/update/{id}
    @GetMapping("/update/{id}")
    public String updateTopic(@PathVariable("id") Long id, Model model) {
        Topic topic = topicService.findTopicByID(id);
        Long sectionId = topic.getSection().getSection_id();

        model.addAttribute("topic", topic);
        model.addAttribute("sectionId", sectionId);

        return "admin/updateTopic";
    }

    @PostMapping("/update")
    public String saveUpdateTopic(@Valid Topic topic, BindingResult bindingResult, Model model, MultipartFile file) {
        String youtubeLinkRegex = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";

        ModelAndView modelAndView = new ModelAndView();
        if (!topic.getVideo_path().matches(youtubeLinkRegex)) {
            bindingResult.rejectValue("video_path", "error.topic",
                    "Invalid youtube link!!");
        }

        model.addAttribute("sectionId", topic.getSection().getSection_id());

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Something went wrong!!");
            return "admin/updateTopic";
        } else {
            Topic oldTopic = topicService.findTopicByID(topic.getId());
            String filePDF = oldTopic.getTopic_readingPDF();

            if (file.getOriginalFilename().equals("")) {
                topic.setTopic_readingPDF(filePDF);
            } else {
                topic.setTopic_readingPDF(file.getOriginalFilename());
                topicPDFService.savePDF(file);
            }
        }

        topicService.saveTopic(topic);
        model.addAttribute("msg", "Update Topic successful!");
        return "admin/updateTopic";
    }

    @GetMapping("/all_topic/{section_id}")
    public ModelAndView allTopic(@PathVariable("section_id") Long id) {
        ModelAndView model = new ModelAndView("admin/all_topic");
        Section findSection = sectionService.findSectionByID(id);
        Course findCourse = courseService.findCourseById(findSection.getCourse().getCourse_id());
        List<Topic> topicList = topicRepository.findAllTopicBySectionId(findSection.getSection_id());

        model.addObject("course", findCourse);
        model.addObject("section", findSection);
        model.addObject("topicList", topicList);
        return model;
    }

    @GetMapping("/single_topic/{id}")
    public ModelAndView singleTopic(@PathVariable("id") Long id) {
        ModelAndView model = new ModelAndView("admin/single_topic");
        Topic topic = topicService.findTopicByID(id);
        Section section = sectionService.findSectionByID(topic.getSection().getSection_id());
        Course course = courseService.findCourseById(section.getCourse().getCourse_id());

        List<Topic> topicList = topicRepository.findAllTopicBySectionId(section.getSection_id());
        List<Comments> commentsList = commentRepository.findAllByDesc(id);

        model.addObject("course1", course);
        model.addObject("section1", section);
        model.addObject("topicList1", topicList);
        model.addObject("topic_single", topic);
        model.addObject("commentList", commentsList);
        return model;
    }
}
