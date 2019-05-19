package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.service.model.ArticleDTO;

public interface ArticleConverter {
    ArticleDTO toDTO(Article article);

    Article toEntity(ArticleDTO articleDTO);
}
