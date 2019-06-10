package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;

import java.util.List;

public interface ArticleRepository extends GenericRepository<Long, Article> {
    List<Article> getLimitArticles(Pagination pagination);

    Integer getCountCommentsById(Long id);
}
