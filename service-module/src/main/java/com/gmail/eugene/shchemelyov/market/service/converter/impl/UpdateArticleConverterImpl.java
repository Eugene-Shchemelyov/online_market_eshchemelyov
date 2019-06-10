package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.service.converter.UpdateArticleConverter;
import com.gmail.eugene.shchemelyov.market.service.model.UpdateArticleDTO;
import org.springframework.stereotype.Component;

@Component
public class UpdateArticleConverterImpl implements UpdateArticleConverter {
    @Override
    public UpdateArticleDTO toDTO(Article article) {
        UpdateArticleDTO updateArticleDTO = new UpdateArticleDTO();
        updateArticleDTO.setId(article.getId());
        updateArticleDTO.setName(article.getName());
        updateArticleDTO.setText(article.getText());
        return updateArticleDTO;
    }
}
