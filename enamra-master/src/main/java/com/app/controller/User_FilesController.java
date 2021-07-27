package com.app.controller;


import com.app.model.User;
import com.app.model.User_files;
import com.app.repository.UserFileRepository;
import com.app.service.UserService;
import com.app.service.User_File_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/files")
public class User_FilesController {

    @Autowired
    private User_File_Service fileService;

    @Autowired
    private UserFileRepository user_file_repository;

    @Autowired
    private UserService userService;

    @GetMapping("/view")
    public ModelAndView viewUserImages(Principal principal) {
        Optional<User> optionalUser = Optional.ofNullable(userService.findUserByEmail(principal.getName()));

        User user = userService.findUserById(optionalUser.get().getId());

        ModelAndView model = new ModelAndView("user/view_img");
        List<User_files> file_img = user_file_repository.last60Records(user.getId());
        model.addObject("img_Lists", file_img);
        return model;
    }

    @Value("${user.file.dir}")
    private String IMAGE_PATH;

    @GetMapping("/load-image/{file_name}")
    public void loadImage(@PathVariable("file_name") String fileName,
                          HttpServletResponse response) throws IOException {
        File imageFile = new File(IMAGE_PATH + "/" + fileName);

        response.setHeader("Content-Length", String.valueOf(imageFile.length()));
        response.setHeader("Content-Disposition", "inline-filename\"" + imageFile.getName() + "\"");

        Files.copy(imageFile.toPath(), response.getOutputStream());
    }

    @GetMapping("/del/{file_id}")
    public ModelAndView deleteImg(@PathVariable("file_id") Long id) {
        ModelAndView model = new ModelAndView("redirect:/files/view");
        fileService.deleteFile(id);
        model.addObject("msg","Delete successfully");
        return model;
    }


}
