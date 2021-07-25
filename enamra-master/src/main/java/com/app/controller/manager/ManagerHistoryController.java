package com.app.controller.manager;

import com.app.model.Mark;
import com.app.model.User;
import com.app.service.MarkService;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/manager/history")
public class ManagerHistoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private MarkService markService;

    @GetMapping("/{quizId}")
    public ModelAndView ViewHistory(@PathVariable("quizId") int quizId) {
        List<Mark> markList = markService.getMarksByQuizId(quizId);
        ModelAndView model = new ModelAndView();
        model.addObject("markList", markList);
        model.setViewName("page/history");
        return model;
    }
}
