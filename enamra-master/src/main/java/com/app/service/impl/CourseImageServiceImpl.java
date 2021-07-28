package com.app.service.impl;

import com.app.model.Course;
import com.app.repository.CourseRepository;
import com.app.service.CourseImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class CourseImageServiceImpl implements CourseImageService {
    @Autowired
    private CourseRepository courseRepository;

    @Value("${course.file.dir}")
    private String courseImageDir;

    @Override
    public void saveImage(MultipartFile file) {
        try(InputStream in = file.getInputStream()) {
            Files.copy(in, Paths.get(courseImageDir + "/" + file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteImage(String imageUrl) {
        File courseFileInServer = new File(courseImageDir + "/" + imageUrl);
        courseFileInServer.delete();
    }
}
