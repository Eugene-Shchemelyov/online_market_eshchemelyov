package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.CommentRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {
    private static final Logger logger = LoggerFactory.getLogger(CommentRepositoryImpl.class);

    public CommentRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Comment> getCommentsByArticleId(Long articleId) {
        String query = "FROM " + entityClass.getName() + " WHERE F_ARTICLE_ID = " + articleId + " ORDER BY F_DATE DESC";
        Query createdQuery = entityManager.createQuery(query);
        return createdQuery.getResultList();
    }
}
