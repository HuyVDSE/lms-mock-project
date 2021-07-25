package com.app.controller.user;

import com.app.model.Mark;
import com.app.model.Question;
import com.app.model.Quiz;
import com.app.model.User;
import com.app.service.MarkService;
import com.app.service.QuizService;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user/history")
public class UserHistoryController {

    @Autowired
    private UserService userService;

    @Autowired
    private MarkService markService;

    @GetMapping("")
    public ModelAndView ViewHistory(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        List<Mark> markList = markService.getMarksByUserId(user.getId());
        ModelAndView model = new ModelAndView();
        model.addObject("markList", markList);
        model.setViewName("page/history");
        return model;
    }
}
