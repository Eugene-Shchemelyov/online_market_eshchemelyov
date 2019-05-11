package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Comment;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.CommentConverter;
import com.gmail.eugene.shchemelyov.market.service.model.CommentDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class CommentConverterImpl implements CommentConverter {
    @Override
    public Comment toComment(CommentDTO commentDTO, User user) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setUser(user);
        comment.setText(commentDTO.getText());
        comment.setDate(commentDTO.getDate());
        comment.setDisplay(commentDTO.isDisplay());
        comment.setDeleted(commentDTO.isDeleted());
        return comment;
    }

    @Override
    public CommentDTO toCommentDTO(Comment comment, UserDTO userDTO) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setUser(userDTO);
        commentDTO.setText(comment.getText());
        commentDTO.setDate(comment.getDate());
        commentDTO.setDisplay(comment.isDisplay());
        commentDTO.setDeleted(comment.isDeleted());
        return commentDTO;
    }
}
