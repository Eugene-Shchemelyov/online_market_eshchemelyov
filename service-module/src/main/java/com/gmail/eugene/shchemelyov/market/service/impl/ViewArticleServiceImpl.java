package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.ArticleRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.ViewArticleService;
import com.gmail.eugene.shchemelyov.market.service.converter.ViewArticleConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ViewArticleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateArticleConstant.ANNOTATION_MAX_SIZE;

@Service
public class ViewArticleServiceImpl implements ViewArticleService {
    private final PaginationService paginationService;
    private final ArticleRepository articleRepository;
    private final ViewArticleConverter viewArticleConverter;

    @Autowired
    public ViewArticleServiceImpl(
            PaginationService paginationService,
            ArticleRepository articleRepository,
            ViewArticleConverter viewArticleConverter
    ) {
        this.paginationService = paginationService;
        this.articleRepository = articleRepository;
        this.viewArticleConverter = viewArticleConverter;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Pagination getLimitArticles(Integer page, SortEnum sort) {
        Pagination pagination = paginationService.getArticlePagination(page, sort);
        List<Article> articles = articleRepository.getLimitArticles(pagination);
        List<ViewArticleDTO> viewArticleDTOS = articles.stream()
                .map(viewArticleConverter::toDTO)
                .peek(viewArticleDTO -> {
                    viewArticleDTO.setCountComments(
                            articleRepository.getCountCommentsById(viewArticleDTO.getId())
                    );
                    if (viewArticleDTO.getText().length() > ANNOTATION_MAX_SIZE) {
                        viewArticleDTO.setText(viewArticleDTO.getText().substring(0, ANNOTATION_MAX_SIZE + 1));
                    }
                })
                .collect(Collectors.toList());
        pagination.setEntities(viewArticleDTOS);
        return pagination;
    }

    @Override
    @Transactional
    public ViewArticleDTO getById(Long id) {
        Article article = articleRepository.getById(id);
        return viewArticleConverter.toDTO(article);
    }

    @Override
    @Transactional
    public List<ViewArticleDTO> getArticles() {
        List<Article> articles = articleRepository.getAllEntities(false);
        return articles.stream()
                .map(viewArticleConverter::toDTO)
                .collect(Collectors.toList());
    }
}
