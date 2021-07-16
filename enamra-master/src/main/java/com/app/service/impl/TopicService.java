package com.app.service.impl;

import com.app.model.Topic;
import com.app.repository.TopicRepository;
import com.app.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class TopicService implements ITopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Override
    public List<Topic> getAllTopic() {
        return topicRepository.findAll();
    }

    @Override
    public Topic findTopicByID(Long id) {
        return topicRepository.findById(id).get();
    }

    @Value("${topic.file.dir}")
    private String file_uploads_dir;

    @Override
    public void saveTopic(Topic topic) {
        String videoPath = topic.getVideo_path();
        topic.setVideo_path(videoPath.replace("watch?v=", "embed/"));
        topicRepository.save(topic);
    }

    @Override
    public void deleteTopicByID(Long id) {
        Topic topic = findTopicByID(id);
        File file = new File(file_uploads_dir + "/" + topic.getVideo_path());
        file.delete();
        topicRepository.deleteById(id);
    }

    public void fileStore(MultipartFile file, String fileName) {
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, Paths.get(file_uploads_dir + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
