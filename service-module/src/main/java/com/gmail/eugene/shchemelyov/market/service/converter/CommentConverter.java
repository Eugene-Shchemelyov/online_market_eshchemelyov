package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Comment;
import com.gmail.eugene.shchemelyov.market.service.model.CommentDTO;

public interface CommentConverter {
    CommentDTO toDTO(Comment comment);

    Comment toEntity(CommentDTO commentDTO);
}
