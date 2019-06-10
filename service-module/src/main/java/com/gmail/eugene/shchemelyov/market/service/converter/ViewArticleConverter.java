package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.service.model.ViewArticleDTO;

public interface ViewArticleConverter {
    ViewArticleDTO toDTO(Article article);

    Article toEntity(ViewArticleDTO viewArticleDTO);
}
