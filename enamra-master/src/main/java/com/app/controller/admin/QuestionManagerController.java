package com.app.controller.admin;

import com.app.model.Answer;
import com.app.model.Question;
import com.app.model.Section;
import com.app.service.AnswerService;
import com.app.service.MediaTypeService;
import com.app.service.QuestionService;
import com.app.service.impl.SectionService;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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

@Controller
@RequestMapping("/admin/question")
@AllArgsConstructor
public class QuestionManagerController {

    private final SectionService sectionService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ServletContext servletContext;

    @GetMapping("/create/{sectionId}")
    public String loadQuestionPage(@PathVariable("sectionId") Long sectionId,
                                   HttpServletRequest request,
                                   Model model) {
        String msg = request.getParameter("msg");
        model.addAttribute("sectionId", sectionId);
        model.addAttribute("number", 4);
        if (msg != null && !msg.equals("")) {
            model.addAttribute("msg", msg);
        }

        return findPaginated(1, sectionId, "", model);
    }

    @GetMapping("page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo") int pageNo,
                                @RequestParam("sectionId") Long sectionId,
                                @RequestParam("txtSearch") String question_content,
                                Model model) {
        int pageSize = 5;
        Page<Question> page = questionService.findPaginatedBySection(pageNo, pageSize, sectionId, "");
        List<Question> listQuestions = page.getContent();
        model.addAttribute("txtSearch", question_content);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("questionList", listQuestions);
        model.addAttribute("number", 4);
        model.addAttribute("sectionId", sectionId);

        return "admin/create_question";
    }

    @GetMapping("/change_number")
    public String changerNumber(@RequestParam("sectionId") Long sectionId,
                                HttpServletRequest request,
                                Model model) {
        int number = Integer.parseInt(request.getParameter("NumofQues"));
        String question_content = "";
        int pageNo = 1;
        int pageSize = 5;
        Page<Question> page = questionService.findPaginatedBySection(pageNo, pageSize, sectionId, question_content);
        List<Question> questionList = page.getContent();

        model.addAttribute("sectionId", sectionId);
        model.addAttribute("number", number);
        model.addAttribute("questionList", questionService.getQuestionsBySectionId(sectionId));

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        return "admin/create_question";
    }

    @PostMapping("/create_new_question")
    public String createNewQuestion(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        int id = 0;
        if (questionService.getLastID() < 0) {
            id += 1;
        } else {
            id = questionService.getLastID() + 1;
        }
        String question = request.getParameter("question");
        String[] answer = request.getParameterValues("answer");
        int correct = Integer.parseInt(request.getParameter("correctAns"));
        Long section = Long.parseLong(request.getParameter("sectionId"));
        long millis = System.currentTimeMillis();
        boolean check = questionService.findByQuestion(question);
        if (!check) {
            Question ques = new Question();
            ques.setQuestionID(id);
            ques.setQuestion(question);
            ques.setCreateDate(new java.sql.Date(millis));
            ques.setStatus("Active");
            ques.setSection(sectionService.findSectionByID(section));

            for (int i = 0; i < answer.length; i++) {
                Answer ans = new Answer();
                ans.setAnswer(answer[i]);
                if (i + 1 == correct) {
                    ans.setStatus(true);
                } else {
                    ans.setStatus(false);
                }
                ques.addAnswer(ans);
            }
            questionService.saveQuestion(ques);
        }

        redirectAttrs.addAttribute("msg", "Create question successfully!");
        return "redirect:/admin/question/create/" + section;
    }

    @PostMapping("/update")
    @Transactional
    public String updateQuestion(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        Long sectionId = Long.parseLong(request.getParameter("sectionId"));
        int questionId = Integer.parseInt(request.getParameter("questionId"));
        String questionContent = request.getParameter("questionContent");
        String[] answerArr = request.getParameterValues("answer" + questionId);
        int correctAnswer = Integer.parseInt(request.getParameter("answer"));
        String questionStatus = request.getParameter("questionStatus");

        Question ques = questionService.findById(questionId);
        ques.setQuestion(questionContent);
        ques.setStatus(questionStatus);
        questionService.saveQuestion(ques);

        List<Answer> answerList = answerService.getAnswersByQuestionId(questionId);
        for (int i = 0; i < answerList.size(); i++) {
            Answer ans = answerList.get(i);
            ans.setAnswer(answerArr[i]);
            if (i + 1 == correctAnswer) {
                ans.setStatus(true);
            } else ans.setStatus(false);
            answerService.saveAnswer(ans);
        }
        redirectAttrs.addAttribute("msg", "Update question successfully!");
        return "redirect:/admin/question/create/" + sectionId;
    }

    @GetMapping("/deleteQuestion/{questionId}/{sectionId}")
    public String deleteQuestion(HttpServletRequest request, @PathVariable("questionId") int questionId,
                                 @PathVariable("sectionId") Long sectionId) {

        questionService.deleteQuestion(questionId);

        request.setAttribute("msg", "Delete question successfully!");
        return "redirect:/admin/question/create/" + sectionId;
    }

    @PostMapping("create_by_file")
    public String createByFile(MultipartFile file, HttpServletRequest request, RedirectAttributes redirectAttrs) throws IOException {
        Long sectionId = Long.parseLong(request.getParameter("sectionId"));
        byte[] bytes = file.getBytes();
        String name = file.getOriginalFilename();
        Path path = Paths.get("./src/main/resources/static/upload_files/" + name);
        try {
            Files.write(path, bytes);
        } catch (FileSystemException ex) {
            redirectAttrs.addAttribute("msg", "Can not load file!");
        }
        List<Question> listQuestions = readQuestionsFromExcelFile(path.toString(), sectionId);
        List<Question> listDeleteQuestions = new ArrayList<Question>();
        for (Question question : listQuestions) {
            boolean check = questionService.findByQuestion(question.getQuestion());
            if (!check) {
                questionService.saveQuestion(question);
            } else {
                listDeleteQuestions.add(question);
            }
        }
        List<Answer> listAnss = readAnswersFromExcelFile(path.toString());
        for (Question question : listDeleteQuestions) {
            listQuestions.remove(question);
        }
        for (Answer answer : listAnss) {
            for (Question question : listQuestions) {
                try {
                    if (question.getQuestionID() == answer.getQuestion().getQuestionID()) {
                        answerService.saveAnswer(answer);
                    }
                } catch (Exception e) {
                }
            }
        }
        try {
            Files.deleteIfExists(path);
        } catch (FileSystemException ex) {
        }
        redirectAttrs.addAttribute("msg", "Create question successfully!");
        return "redirect:/admin/question/create/" + sectionId;
    }

    public List<Question> readQuestionsFromExcelFile(String excelFilePath, Long sectionId) throws IOException {
        List<Question> listQuestions = new ArrayList<Question>();
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workBook = getWorkbook(inputStream, excelFilePath);
        Sheet firstSheet = workBook.getSheetAt(0);
        Iterator<Row> rows = firstSheet.iterator();

        while (rows.hasNext()) {
            try {
                Row row = rows.next();
                if (row.getRowNum() == 0) {
                    continue;
                }
                Iterator<Cell> cells = row.cellIterator();
                Question question = new Question();
                boolean flag = true;
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            question.setQuestionID(Integer.parseInt((getCellValue(cell) + "")));
                            break;
                        case 1:
                            String test = (String) getCellValue(cell);
                            if (test.equals("") || test == null) {
                                flag = false;
                            }
                            question.setQuestion((String) getCellValue(cell));
                            break;
                        case 2:
                            question.setStatus((String) getCellValue(cell));
                            break;
                    }
                }
                Section section = sectionService.findSectionByID(sectionId);
                question.setSection(section);
                long millis = System.currentTimeMillis();
                question.setCreateDate(new java.sql.Date(millis));
                if (flag) listQuestions.add(question);
            } catch (Exception e) {
                continue;
            }
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
            try {
                Row row = rows.next();
                if (row.getRowNum() == 0) {
                    continue;
                }
                Iterator<Cell> cells = row.cellIterator();
                Answer answer = new Answer();
                //String listanswers[] = null;
                boolean flag = true;
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            int questionId = Integer.parseInt((getCellValue(cell) + ""));
                            Question question = questionService.findById(questionId);
                            answer.setQuestion(question);
                            break;
                        case 1:
                            String questCon = (String) getCellValue(cell);

                            if (questCon.equals("") || questCon == null) {
                                flag = false;
                            }
                            break;

                        case 3:
//                            String answers = (String) getCellValue(cell);
//                            listanswers = answers.split(",");
                            answer.setAnswer((String) getCellValue(cell));
                            break;
                        case 4:
                            answer.setStatus((boolean) getCellValue(cell));
                            break;
                    }
                }
//                for (String ans : listanswers) {
//                    Answer x = new Answer();
//                    x.setQuestion(answer.getQuestion());
//                    x.setAnswer(ans);
//                    x.setStatus(answer.isStatus());
//                }
                if (flag) listAnswers.add(answer);
            } catch (Exception e) {
                continue;
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

    @GetMapping("/search_question_by_content/{pageNo}")
    public ModelAndView SearchQuestion(@RequestParam("sectionId") Long sectionId,
                                       @RequestParam("txtSearch") String question_content,
                                       @PathVariable("pageNo") int pageNo) {
        ModelAndView model = new ModelAndView();
        model.setViewName("admin/create_question");
        int pageSize = 5;
        if (question_content == null || question_content.equals("null"))  {
            question_content = "";
        }
        Page<Question> page = questionService.findPaginatedBySection(pageNo, pageSize, sectionId, question_content);
        List<Question> questionList = page.getContent();
        model.addObject("questionList", questionList);
        model.addObject("sectionId", sectionId);
        model.addObject("number", 4);
        model.addObject("txtSearch", question_content);

        model.addObject("currentPage", pageNo);
        model.addObject("totalPages", page.getTotalPages());
        model.addObject("totalItems", page.getTotalElements());

        return model;
    }
}
