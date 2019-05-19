package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Comment;

import java.util.List;

public interface CommentRepository extends GenericRepository<Long, Comment> {
    List<Comment> getCommentsByArticleId(Long articleId);
}
