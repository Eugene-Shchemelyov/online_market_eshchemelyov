package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.service.model.NewArticleDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UpdateArticleDTO;

public interface ArticleService {
    UpdateArticleDTO getById(Long id);

    void deleteById(Long id);

    NewArticleDTO add(NewArticleDTO newArticleDTO, Long userId);

    void updateCountViews(Long id);

    void update(UpdateArticleDTO updateArticleDTO);
}
