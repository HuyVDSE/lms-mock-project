package com.app.controller.admin;


import com.app.model.Answer;
import com.app.model.Question;
import com.app.model.Section;
import com.app.model.User;
import com.app.repository.QuestionRepo;
import com.app.service.*;
import com.app.service.impl.SectionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.poi.ss.usermodel.Row;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {


    String name;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private SectionService sectionService;

    @GetMapping("/home")
    public String homeAdmin() {
        return "admin/home";
    }


    @GetMapping("/signup")
    public ModelAndView createadminPage() {
        ModelAndView model = new ModelAndView();
        model.addObject("user", new User());
        model.setViewName("admin/signup");
        return model;

    }

    @GetMapping("/create_question")
    public ModelAndView test() {
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/create_question");
        model.addObject("number", 4);
        model.addObject("sections", sectionService.getAllSection());
        return model;
    }

    @PostMapping("/create_new_question")
    public ModelAndView createNewQuestion(HttpServletRequest request) {
        int id = questionService.getLastID() + 1;
        String question = request.getParameter("question");
        String[] answer = request.getParameterValues("answer");
        int correct = Integer.parseInt(request.getParameter("correctAns"));
        Long section = Long.parseLong(request.getParameter("section"));
        long millis = System.currentTimeMillis();

        Question ques = new Question();
        ques.setQuestionID(id);
        ques.setQuestion(question);
        ques.setCreateDate(new java.sql.Date(millis));
        ques.setStatus("Active");
        ques.setSection(sectionService.findSectionByID(section));
        questionService.saveQuestion(ques);

        for (int i = 0; i < answer.length; i++) {
            Answer ans = new Answer();
            ans.setAnswer(answer[i]);
            if (i + 1 == correct) {
                ans.setStatus(true);
            } else {
                ans.setStatus(false);
            }
            Question quess = questionService.findById(id);
            ans.setQuestion(quess);
            answerService.saveAnswer(ans);
        }

        ModelAndView model = new ModelAndView();
        model.setViewName("admin/create_question");
        model.addObject("msg", "Create question successfully!");
        model.addObject("number", 4);
        model.addObject("sections", sectionService.getAllSection());
        return model;
    }

    @GetMapping("/change_number")
    public ModelAndView changerNumber(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        int number = Integer.parseInt(request.getParameter("NumofQues"));
        model.setViewName("admin/create_question");
        model.addObject("number", number);
        model.addObject("sections", sectionService.getAllSection());
        return model;
    }

    @PostMapping("/signup")
    public ModelAndView signup(@Valid User user, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            bindingResult.rejectValue("password", "error.user", "Password not match!!!!");
        }

        if (bindingResult.hasErrors()) {
            model.setViewName("admin/signup");
        } else {
            adminService.saveAdmin(user);
            model.addObject("msg", "User has been registered successfully!");
            model.addObject("user", new User());
            model.setViewName("admin/signup");
        }
        return model;

    }


    @GetMapping("/list")
    public ModelAndView adminList() {
        ModelAndView model = new ModelAndView("admin/home");
        List<User> adminList = adminService.getAllAdmin();
        model.addObject("adminLIST", adminList);
        return model;
    }

    @GetMapping("/edit/{id}")
    public String loadUserDetailForUpdate(@PathVariable("id") Long id, Model model) {
        User currentUser = userService.findUserById(id);
        model.addAttribute("user", currentUser);
        return "admin/edit";
    }

    @PostMapping("/edit")
    public String updateUserDetail(@Valid User user, BindingResult bindingResult,
                                   HttpServletRequest request, Model model) {
        User userUpdate = userService.findUserByEmail(user.getEmail());
        String oldPassword = request.getParameter("oldPassword");

        if (user.getFirstname().equals("")) {
            bindingResult.rejectValue("firstname", "error.user", "First name must not empty!");
        } else if (user.getLastname().equals("")) {
            bindingResult.rejectValue("lastname", "error.user", "Last name must not empty!");
        } else if (oldPassword == null || !encoder.matches(oldPassword, userUpdate.getPassword())) {
            model.addAttribute("passworderror", "Current password didn't match!!!!");
        } else if (user.getPassword().equals("")) {
            bindingResult.rejectValue("password", "error.user", "Password must not empty!");
        } else {
            if (!user.getPassword().equals(user.getConfirmPassword())) {
                bindingResult.rejectValue("password", "error.user", "Confirm Password didnt match!!!!");
            }

            if (bindingResult.hasErrors()) {
                return "admin/edit";
            }

            userService.saveUser(user, userUpdate.getRoles());
            model.addAttribute("msg", "Update user successfully!");
            model.addAttribute("user", new User());

        }
        return "admin/edit";
    }

    @PostMapping("create_by_file")
    public ModelAndView createByFile(MultipartFile file) throws IOException {
        ModelAndView model = new ModelAndView("admin/create_question");
        byte[] bytes = file.getBytes();
        String name = file.getOriginalFilename();
        Path path = Paths.get("./src/main/resources/static/upload_files/" + name);
        try {
            Files.write(path, bytes);
        } catch (FileSystemException ex) {
            model.addObject("msg", "Can not load file!");
        }
        List<Question> listQuestions = readQuestionsFromExcelFile(path.toString());
        for (Question question : listQuestions) {
            boolean check = questionService.findByQuestion(question.getQuestion());
            if (!check) {
                questionService.saveQuestion(question);
            }
        }
        List<Answer> listAnss = readAnswersFromExcelFile(path.toString());
        for (Answer answer : listAnss) {
            answerService.saveAnswer(answer);
        }
        try {
            Files.deleteIfExists(path);
        } catch (FileSystemException ex) {
        }
        model.addObject("msg", "Create question successfully!");
        model.addObject("number", 4);
        model.addObject("sections", sectionService.getAllSection());
        return model;
    }

    public List<Question> readQuestionsFromExcelFile(String excelFilePath) throws IOException {
        List<Question> listQuestions = new ArrayList<Question>();
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workBook = getWorkbook(inputStream, excelFilePath);
        Sheet firstSheet = workBook.getSheetAt(0);
        Iterator<Row> rows = firstSheet.iterator();

        while (rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() == 0) {
                continue;
            }
            Iterator<Cell> cells = row.cellIterator();
            Question question = new Question();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                int columnIndex = cell.getColumnIndex();

                switch (columnIndex) {
                    case 0:
                        question.setQuestionID(Integer.parseInt((getCellValue(cell) + "")));
                        break;
                    case 1:
                        question.setQuestion((String) getCellValue(cell));
                        break;
                    case 2:
                        question.setStatus((String) getCellValue(cell));
                        break;
                    case 3:
                        Section section = sectionService.findSectionByID(Long.parseLong(getCellValue(cell) + ""));
                        question.setSection(section);
                        break;
                }
            }
            long millis = System.currentTimeMillis();
            question.setCreateDate(new java.sql.Date(millis));
            listQuestions.add(question);
        }

        workBook.close();
        inputStream.close();

        return listQuestions;
    }

    public List<Answer> readAnswersFromExcelFile(String excelFilePath) throws IOException {
        List<Answer> listAnswers = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workBook = getWorkbook(inputStream, excelFilePath);
        Sheet firstSheet = workBook.getSheetAt(0);
        Iterator<Row> rows = firstSheet.iterator();

        while (rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() == 0) {
                continue;
            }
            Iterator<Cell> cells = row.cellIterator();
            Answer answer = new Answer();
            String listanswers[] = null;
            while (cells.hasNext()) {
                Cell cell = cells.next();
                int columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case 0:
                        int questionId = Integer.parseInt((getCellValue(cell) + ""));
                        Question question = questionService.findById(questionId);
                        answer.setQuestion(question);
                        break;
                    case 4:
                        String answers = (String) getCellValue(cell);
                        listanswers = answers.split(",");
                        break;
                    case 5:
                        answer.setStatus((boolean) getCellValue(cell));
                        break;
                }
            }
            for (String ans : listanswers) {
                Answer x = new Answer();
                x.setQuestion(answer.getQuestion());
                x.setAnswer(ans);
                x.setStatus(answer.isStatus());
                listAnswers.add(x);
            }
        }
        workBook.close();
        inputStream.close();
        return listAnswers;
    }

    private Workbook getWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook = null;

        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();

            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();

            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
        }

        return null;
    }

    @RequestMapping("/download_template")
    public ResponseEntity<InputStreamResource> downloadTemplate() throws IOException {
        String fileName = "Template_Quiz.xlsx";
        String directory = "./src/main/resources/static/upload_files/";
        MediaType mediaType = MediaTypeService.getMediaTypeForFileName(this.servletContext, fileName);

        File file = new File(directory + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }

//    @RequestMapping("/change_number")
//    public ModelAndView changeNumber(HttpServletRequest request) throws IOException {
//        ModelAndView model = new ModelAndView("admin/create_question");
//        int number = Integer.parseInt(request.getParameter("NumofQues"));
//        System.out.println(number);
//        return model;
//    }
}
