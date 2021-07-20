package com.app.controller.admin;


import com.app.model.Answer;
import com.app.model.Question;
import com.app.model.User;
import com.app.service.AdminService;
import com.app.service.AnswerService;
import com.app.service.QuizService;
import com.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    private QuizService quizService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/home")
    public String homeAdmin() {
        return "admin/home";
    }

    @GetMapping("/signup")
    public ModelAndView createAdminPage() {
        ModelAndView model = new ModelAndView();
        model.addObject("user", new User());
        model.setViewName("admin/signup");
        return model;

    }

    @GetMapping("/create_question")
    public ModelAndView test() {
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/create_question");
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
        List<Question> listQuestions = readQuizsFromExcelFile(path.toString());
        for (Question question : listQuestions) {
            boolean check = quizService.findByQuestion(question.getQuestion());
            if (!check) {
                quizService.saveQuiz(question);
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
        return model;
    }

    public List<Question> readQuizsFromExcelFile(String excelFilePath) throws IOException {
        List<Question> listQuestions = new ArrayList<Question>();
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workBook = getWorkbook(inputStream, excelFilePath);
        Sheet firstSheet = workBook.getSheetAt(0);
        Iterator<Row> rows = firstSheet.iterator();

        while (rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum()==0){
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
                        question.setSubjectID((String) getCellValue(cell));
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
        List<Answer> listAnss = new ArrayList<Answer>();
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workBook = getWorkbook(inputStream, excelFilePath);
        Sheet firstSheet = workBook.getSheetAt(0);
        Iterator<Row> rows = firstSheet.iterator();
        while (rows.hasNext()) {

            Row row = rows.next();
            if (row.getRowNum()==0){
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
                        answer.setQuestionID(Integer.parseInt((getCellValue(cell) + "")));
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
                x.setQuestionID(answer.getQuestionID());
                x.setAnswer(ans);
                x.setStatus(answer.isStatus());
                listAnss.add(x);
            }
        }
        workBook.close();
        inputStream.close();
        return listAnss;
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
}
