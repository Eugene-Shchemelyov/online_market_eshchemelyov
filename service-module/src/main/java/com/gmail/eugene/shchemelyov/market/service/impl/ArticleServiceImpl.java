package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.ArticleRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import com.gmail.eugene.shchemelyov.market.service.ArticleService;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.converter.ArticleConverter;
import com.gmail.eugene.shchemelyov.market.service.exception.ServiceException;
import com.gmail.eugene.shchemelyov.market.service.model.ArticleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;
    private final PaginationService paginationService;
    private final UserRepository userRepository;

    @Autowired
    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            ArticleConverter articleConverter,
            PaginationService paginationService,
            UserRepository userRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleConverter = articleConverter;
        this.paginationService = paginationService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Pagination getLimitArticles(Integer page, SortEnum sort) {
        Pagination pagination = paginationService.getArticlePagination(page, sort);
        List<Article> articles = articleRepository.getLimitArticles(pagination);
        List<ArticleDTO> articleDTOS = articles.stream()
                .map(articleConverter::toDTO)
                .collect(Collectors.toList());
        pagination.setEntities(articleDTOS);
        return pagination;
    }

    @Override
    @Transactional
    public ArticleDTO getById(Long id) {
        Article article = articleRepository.getById(id);
        return articleConverter.toDTO(article);
    }

    @Override
    @Transactional
    public List<ArticleDTO> getArticles() {
        List<Article> articles = articleRepository.getAllEntities();
        return articles.stream()
                .map(articleConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ArticleDTO add(ArticleDTO articleDTO) {
        String date = getConvertedDate(articleDTO.getDate());
        articleDTO.setDate(date);
        Article article = articleConverter.toEntity(articleDTO);
        User user = userRepository.getById(articleDTO.getUser().getId());
        article.setUser(user);
        article.setCountViews(0L);
        articleRepository.create(article);
        return articleConverter.toDTO(article);
    }

    @Override
    @Transactional
    public void updateCountViews(Long id) {
        Article article = articleRepository.getById(id);
        article.setCountViews(article.getCountViews() + 1L);
        articleRepository.update(article);
    }

    @Override
    @Transactional
    public void update(ArticleDTO articleDTO) {
        Article article = articleRepository.getById(articleDTO.getId());
        article.setName(articleDTO.getName());
        article.setAnnotation(articleDTO.getAnnotation());
        article.setText(articleDTO.getText());
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date().getTime());
        article.setDate(date);
        articleRepository.update(article);
    }

    private String getConvertedDate(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
            Date converterDate = simpleDateFormat.parse(date);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(converterDate);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format("%s: %s.", "Bad date format when converting the date", date), e);
        }
    }
}
