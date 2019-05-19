package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.CommentRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Comment;
import com.gmail.eugene.shchemelyov.market.service.CommentService;
import com.gmail.eugene.shchemelyov.market.service.converter.CommentConverter;
import com.gmail.eugene.shchemelyov.market.service.model.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

    @Autowired
    public CommentServiceImpl(
            CommentRepository commentRepository,
            CommentConverter commentConverter
    ) {
        this.commentRepository = commentRepository;
        this.commentConverter = commentConverter;
    }

    @Override
    @Transactional
    public List<CommentDTO> getCommentsByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.getCommentsByArticleId(articleId);
        return comments.stream()
                .map(commentConverter::toDTO)
                .collect(Collectors.toList());
    }
}
