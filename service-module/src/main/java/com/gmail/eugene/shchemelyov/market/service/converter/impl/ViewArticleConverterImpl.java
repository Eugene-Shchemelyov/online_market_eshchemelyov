package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.service.converter.CommentConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.ViewArticleConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ViewArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ViewArticleConverterImpl implements ViewArticleConverter {
    private final CommentConverter commentConverter;

    @Autowired
    public ViewArticleConverterImpl(
            CommentConverter commentConverter
    ) {
        this.commentConverter = commentConverter;
    }

    @Override
    public ViewArticleDTO toDTO(Article article) {
        ViewArticleDTO viewArticleDTO = new ViewArticleDTO();
        viewArticleDTO.setId(article.getId());
        viewArticleDTO.setUserName(article.getUser().getName());
        viewArticleDTO.setUserSurname(article.getUser().getSurname());
        viewArticleDTO.setDate(article.getDate());
        viewArticleDTO.setName(article.getName());
        viewArticleDTO.setCountViews(article.getCountViews());
        viewArticleDTO.setText(article.getText());
        viewArticleDTO.setCountComments(article.getComments().size());
        viewArticleDTO.setComments(article.getComments().stream()
                .map(commentConverter::toDTO)
                .collect(Collectors.toList()));
        return viewArticleDTO;
    }

    @Override
    public Article toEntity(ViewArticleDTO viewArticleDTO) {
        Article article = new Article();
        article.setId(viewArticleDTO.getId());
        article.setDate(viewArticleDTO.getDate());
        article.setName(viewArticleDTO.getName());
        article.setText(viewArticleDTO.getText());
        article.setCountViews(viewArticleDTO.getCountViews());
        return article;
    }
}