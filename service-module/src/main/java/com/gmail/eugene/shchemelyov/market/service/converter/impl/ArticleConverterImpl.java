package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.service.converter.ArticleConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.CommentConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.UserConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ArticleConverterImpl implements ArticleConverter {
    private final UserConverter userConverter;
    private final CommentConverter commentConverter;

    @Autowired
    public ArticleConverterImpl(
            UserConverter userConverter,
            CommentConverter commentConverter
    ) {
        this.userConverter = userConverter;
        this.commentConverter = commentConverter;
    }

    @Override
    public ArticleDTO toDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setUser(userConverter.toDTO(article.getUser()));
        articleDTO.setDate(article.getDate());
        articleDTO.setName(article.getName());
        articleDTO.setAnnotation(article.getAnnotation());
        articleDTO.setText(article.getText());
        articleDTO.setCountViews(article.getCountViews());
        articleDTO.setComments(
                article.getComments().stream()
                        .map(commentConverter::toDTO)
                        .collect(Collectors.toList())
        );
        return articleDTO;
    }

    @Override
    public Article toEntity(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setId(articleDTO.getId());
        article.setUser(userConverter.toEntity(articleDTO.getUser()));
        article.setDate(articleDTO.getDate());
        article.setName(articleDTO.getName());
        article.setAnnotation(articleDTO.getAnnotation());
        article.setText(articleDTO.getText());
        article.setCountViews(articleDTO.getCountViews());
        article.setComments(
                articleDTO.getComments().stream()
                        .map(commentConverter::toEntity)
                        .collect(Collectors.toList())
        );
        return article;
    }
}