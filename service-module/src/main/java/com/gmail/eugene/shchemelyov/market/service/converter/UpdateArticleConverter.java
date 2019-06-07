package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.service.model.UpdateArticleDTO;

public interface UpdateArticleConverter {
    UpdateArticleDTO toDTO(Article article);
}
