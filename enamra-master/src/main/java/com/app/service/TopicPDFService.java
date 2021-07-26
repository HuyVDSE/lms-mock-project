package com.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface TopicPDFService {

        void savePDF(MultipartFile file);

        void deletePDF(String PdfFile);
}
