package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import com.gmail.eugene.shchemelyov.market.service.model.ArticleDTO;

import java.util.List;

public interface ArticleService {
    Pagination getLimitArticles(Integer page, SortEnum sort);

    ArticleDTO getById(Long id);

    List<ArticleDTO> getArticles();

    void deleteById(Long id);

    void add(ArticleDTO articleDTO);

    void updateCountViews(Long id);
}