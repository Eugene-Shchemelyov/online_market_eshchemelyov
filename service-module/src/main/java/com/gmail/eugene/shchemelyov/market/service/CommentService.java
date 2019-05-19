package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.service.model.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getCommentsByArticleId(Long id);
}
