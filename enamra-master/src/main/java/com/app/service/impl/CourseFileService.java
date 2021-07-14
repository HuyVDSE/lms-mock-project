package com.app.service.impl;

import com.app.model.CourseFile;
import com.app.repository.CourseFileRepository;
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
public class CourseFileService {

    @Autowired
    private CourseFileRepository courseFileRepo;

    @Value("${course.file.dir}")
    private String courseFileDir;

    //upload and store it to server
    private void storeFile(MultipartFile file, String modifiedFilename) {
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, Paths.get(courseFileDir + "/" + modifiedFilename), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFile(MultipartFile file, CourseFile courseFile) {
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        storeFile(file, fileName);
        courseFile.setFileName(fileName);
        courseFile.setFileExtension(fileExtension);
        courseFile.setFilePath(courseFileDir + "/" + fileName);
        courseFileRepo.save(courseFile);
    }

    public void deleteFile(Integer id) {
        CourseFile courseFile = courseFileRepo.getOne(id);
        File fileInServer = new File(courseFile.getFilePath());
        fileInServer.delete();
        courseFileRepo.delete(courseFile);
    }

    public CourseFile getFileByCourseId(Long id) {
        return courseFileRepo.getCourseFileByCourse(id);
    }
}
