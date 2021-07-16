package com.app.service.impl;


import com.app.model.Comments;
import com.app.repository.CommentRepository;
import com.app.service.ICommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService implements ICommentsService {

    @Autowired
    private CommentRepository commentRepository;


    @Override
    public List<Comments> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comments getCommentsByID(Long id) {
        return commentRepository.findById(id).get();
    }

    @Override
    public void saveComment(Comments comments) {
        commentRepository.save(comments);
    }

    @Override
    public void deleteComments(Long id) {
        commentRepository.deleteById(id);
    }
}
