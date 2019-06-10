package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import com.gmail.eugene.shchemelyov.market.service.model.ViewArticleDTO;

import java.util.List;

public interface ViewArticleService {
    Pagination getLimitArticles(Integer page, SortEnum sort);

    ViewArticleDTO getById(Long id);

    List<ViewArticleDTO> getArticles();
}
