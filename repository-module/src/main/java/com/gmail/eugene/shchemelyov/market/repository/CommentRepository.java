package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Comment;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;

import java.sql.Connection;
import java.util.List;

public interface CommentRepository extends GenericRepository {
    List<Comment> getLimitComments(Connection connection, Pagination pagination);

    void deleteCommentById(Connection connection, Long id, Boolean isDeleted);

    Comment getCommentById(Connection connection, Long id);

    void changeDisplay(Connection connection, Comment comment);
}
