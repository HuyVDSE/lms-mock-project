package com.app.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CourseImageService {

    void saveImage(MultipartFile file);
}
