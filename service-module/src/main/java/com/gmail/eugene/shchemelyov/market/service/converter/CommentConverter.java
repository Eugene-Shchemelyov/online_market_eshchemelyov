package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Comment;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.model.CommentDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;

public interface CommentConverter {
    Comment toComment(CommentDTO commentDTO, User user);

    CommentDTO toCommentDTO(Comment comment, UserDTO userDTO);
}
