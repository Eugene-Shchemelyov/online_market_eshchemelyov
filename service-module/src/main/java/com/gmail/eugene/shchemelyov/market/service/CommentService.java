package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.model.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getComments(Pagination pagination);

    void changeCommentsDisplay(List<Long> commentsId);

    void delete(Long id);
}
