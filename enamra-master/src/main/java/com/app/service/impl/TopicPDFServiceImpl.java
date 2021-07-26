package com.app.service.impl;

import com.app.repository.CourseRepository;
import com.app.service.TopicPDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class TopicPDFServiceImpl implements TopicPDFService {

    @Value("${topic.file.dir}")
    private String topicPDFDir;

    @Override
    public void savePDF(MultipartFile file) {
        try(InputStream in = file.getInputStream()) {
            Files.copy(in, Paths.get(topicPDFDir + "/" + file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePDF(String PdfFile) {

    }
}
