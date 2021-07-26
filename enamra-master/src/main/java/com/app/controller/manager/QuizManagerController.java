package com.app.controller.manager;


import com.app.model.Question;
import com.app.model.QuestionForQuiz;
import com.app.model.Quiz;
import com.app.model.Section;
import com.app.service.QuestionForQuizService;
import com.app.service.QuestionService;
import com.app.service.QuizService;
import com.app.service.UserService;
import com.app.service.impl.SectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/manager/quiz")
@AllArgsConstructor
public class QuizManagerController {
    private final QuizService quizService;
    private final QuestionService questionService;
    private final SectionService sectionService;
    private final QuestionForQuizService questionForQuizService;
    private final UserService userService;
    @GetMapping("/create_quiz/{sectionId}")
    public ModelAndView ViewPage(HttpServletRequest request, @PathVariable("sectionId") Long sectionId) {
        String msg = request.getParameter("msg");
        ModelAndView model = new ModelAndView();
        Section section = sectionService.findSectionByID(sectionId);
        model.addObject("section", section);
        model.addObject("sectionId",sectionId);
        model.setViewName("page/QuizManager");
        if (msg != null && !msg.equals("")) {
            model.addObject("msg", msg);
        }
        model.addObject("quizList",quizService.getQuizsBySectionId(sectionId));
        return model;
    }
    @GetMapping("/create_new_quiz/{sectionId}")
    public ModelAndView CreateQuiz(@PathVariable("sectionId") Long sectionId,
                                   HttpServletRequest request) {
        String msg = request.getParameter("msg");
        ModelAndView model = new ModelAndView();
        model.addObject("sectionId", sectionId);
        model.addObject("Listsize",questionService.getQuestionsBySectionIdActive(sectionId).size());
        model.setViewName("manager/CreateQuiz");
//        model.addObject("questionList",questionService.getQuestionsBySectionIdActive(sectionId));
        if (msg != null && !msg.equals("")) {
            model.addObject("msg", msg);
        }

        return model;
    }
    @PostMapping("/create_new_quiz")
    public ModelAndView CreateAQuiz(HttpServletRequest request) throws ParseException {
        String quizName = request.getParameter("txtQuizName");
        int numOfQues = Integer.parseInt(request.getParameter("NumofQues"));
        String timeOfQuiz = request.getParameter("TimeOfQuiz");
        String startDate = request.getParameter("trip_start");
        String startTime = request.getParameter("appt_time");
        String endDate = request.getParameter("trip_end");
        String endTime = request.getParameter("end_time");
        Long sectionId = Long.parseLong(request.getParameter("sectionId"));

        List<Question> newquiz = questionService.getRanDomQuestion(sectionId,numOfQues);
        Quiz quiz = new Quiz();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        quiz.setEndDate(new java.sql.Timestamp(format.parse(endDate+" "+endTime).getTime()));

        quiz.setName(quizName);
        quiz.setStartDate(new java.sql.Timestamp(format.parse(startDate+" "+startTime).getTime()));
        String Gmail = request.getParameter("user_Gmail");
        quiz.setUser(userService.findUserByEmail(Gmail));
        quiz.setTotalTime(Integer.parseInt(timeOfQuiz));
        quiz.setSection(sectionService.findSectionByID(sectionId));
//
//        quiz.setQuizId(quizService.getLastID());
        quizService.saveQuiz(quiz);
        String msg = request.getParameter("msg");


        System.out.println(newquiz.size());
        for (Question question: newquiz){
            QuestionForQuiz questionForQuiz = new QuestionForQuiz();
            questionForQuiz.setQuiz(quiz);
            questionForQuiz.setQuestion(question);
            questionForQuizService.saveQuestionForQuiz(questionForQuiz);
        }
        ModelAndView model = new ModelAndView();
        model.addObject("Listsize",questionService.getQuestionsBySectionId(sectionId).size());
        model.addObject("msg","Create quiz successfully");
        model.setViewName("manager/CreateQuiz");
        model.addObject("sectionId",sectionId);
        System.out.println("DONE");
        return model;
    }

    @GetMapping("/delete_quiz/{quizId}/{sectionId}")
    public ModelAndView DeleteQuiz(HttpServletRequest request, @PathVariable("sectionId") Long sectionId,@PathVariable("quizId") int quizId) {
        String msg = request.getParameter("msg");
        ModelAndView model = new ModelAndView();
        model.addObject("sectionId",sectionId);
        model.setViewName("page/QuizManager");
        Quiz quiz = quizService.getQuizById(quizId);

        List<QuestionForQuiz> listQuestion = questionForQuizService.getAllByQuizId(quizId);
        for (QuestionForQuiz dto: listQuestion){
            questionForQuizService.deleteQuestionForQuiz(dto);
        }
        quizService.deleteQuiz(quiz);
        if (msg != null && !msg.equals("")) {
            model.addObject("msg", msg);
        }
        Section section = sectionService.findSectionByID(sectionId);
        model.addObject("section", section);
        model.addObject("quizList",quizService.getQuizsBySectionId(sectionId));
        return model;
    }
    @GetMapping("/update_quiz/{quizId}/{sectionId}")
    public ModelAndView UpdateQuiz(HttpServletRequest request, @PathVariable("sectionId") Long sectionId,@PathVariable("quizId") int quizId) {
        String msg = request.getParameter("msg");
        ModelAndView model = new ModelAndView();
        model.setViewName("manager/QuizDetail");


        model.addObject("sectionId",sectionId);
        Quiz quiz = quizService.getQuizById(quizId);
        String[] start = (quiz.getStartDate()+"").split(" ");
        model.addObject("start_time",start[1]);
        model.addObject("start_date",start[0]);
        String[] end = (quiz.getEndDate()+"").split(" ");
        model.addObject("end_time",end[1]);
        model.addObject("end_date",end[0]);

        List<QuestionForQuiz> listQuestion = quiz.getQuestionForQuizList();
        model.addObject("quiz",quiz);
        model.addObject("ListQuestionForQuiz",listQuestion);
        if (msg != null && !msg.equals("")) {
            model.addObject("msg", msg);
        }
        return model;
    }
    @PostMapping("/update_quiz")
    public ModelAndView UpdateQuiz(HttpServletRequest request) throws ParseException {
        int quizId = Integer.parseInt(request.getParameter("txtquizId"));
        String quizName = request.getParameter("txtQuizName");
        String timeOfQuiz = request.getParameter("TimeOfQuiz");
        String startDate = request.getParameter("trip_start");
        String startTime = request.getParameter("appt_time");
        String endDate = request.getParameter("trip_end");
        String endTime = request.getParameter("end_time");
        Long sectionId = Long.parseLong(request.getParameter("sectionId"));

        Quiz quiz = quizService.getQuizByQuizId(quizId);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        quiz.setEndDate(new java.sql.Timestamp(format.parse(endDate+" "+endTime).getTime()));

        quiz.setName(quizName);
        quiz.setStartDate(new java.sql.Timestamp(format.parse(startDate+" "+startTime).getTime()));

        quiz.setTotalTime(Integer.parseInt(timeOfQuiz));

        quizService.updateQuiz(quiz);
        String msg = request.getParameter("msg");

        ModelAndView model = new ModelAndView();
        model.addObject("msg","Update quiz successfully");

//
        List<QuestionForQuiz> listQuestion = quiz.getQuestionForQuizList();
        if (listQuestion.size()<=1){
            for (QuestionForQuiz dto : listQuestion){
                questionForQuizService.deleteQuestionForQuiz(dto);
            }
            quizService.deleteQuiz(quiz);
            model.setViewName("manager/QuizManager");
            Section section = sectionService.findSectionByID(sectionId);
            model.addObject("section", section);
        } else {
            model.setViewName("manager/QuizDetail");
        }
        model.addObject("quiz",quiz);
        model.addObject("ListQuestionForQuiz",listQuestion);
        if (msg != null && !msg.equals("")) {
            model.addObject("msg", msg);
        }
//
        model.addObject("sectionId",sectionId);
        return model;
    }

    @GetMapping("/delete_questionInQuiz/{quizId}/{sectionId}/{questionId}")
    public ModelAndView DeleteQuestionInQuiz(HttpServletRequest request, @PathVariable("sectionId") Long sectionId,@PathVariable("quizId") int quizId,
                                             @PathVariable("questionId") int questionId) {
        String msg = request.getParameter("msg");
        ModelAndView model = new ModelAndView();
        Quiz quiz = quizService.getQuizByQuizId(quizId);
        model.addObject("sectionId",sectionId);
        List<QuestionForQuiz> listQuestion = quiz.getQuestionForQuizList();
        model.addObject("quiz",quiz);

        if (listQuestion.size()<=1){
            for (QuestionForQuiz dto : listQuestion){
                questionForQuizService.deleteQuestionForQuiz(dto);
            }
            quizService.deleteQuiz(quiz);
            model.setViewName("manager/QuizManager");
            Section section = sectionService.findSectionByID(sectionId);
            model.addObject("section", section);
        } else {
            questionForQuizService.deleteQuestionInQuiz(questionId,quizId);
            quiz = quizService.getQuizByQuizId(quizId);
            listQuestion = quiz.getQuestionForQuizList();
            for (QuestionForQuiz x : listQuestion){
                if (x.getQuestion().getQuestionID()==questionId){
                    listQuestion.remove(x);
                    break;
                }
            }
            model.setViewName("manager/QuizDetail");
        }
        model.addObject("ListQuestionForQuiz",listQuestion);
        if (msg != null && !msg.equals("")) {
            model.addObject("msg", msg);
        }

        return model;
    }
}