package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.ArticleRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import com.gmail.eugene.shchemelyov.market.service.ArticleService;
import com.gmail.eugene.shchemelyov.market.service.CommentService;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.converter.ArticleConverter;
import com.gmail.eugene.shchemelyov.market.service.exception.ServiceException;
import com.gmail.eugene.shchemelyov.market.service.model.ArticleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.SERVICE_ERROR_MESSAGE;
import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.TRANSACTION_ERROR_MESSAGE;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;
    private final PaginationService paginationService;
    private final CommentService commentService;

    @Autowired
    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            ArticleConverter articleConverter,
            PaginationService paginationService,
            CommentService commentService
    ) {
        this.articleRepository = articleRepository;
        this.articleConverter = articleConverter;
        this.paginationService = paginationService;
        this.commentService = commentService;
    }

    @Override
    public Pagination getLimitArticles(Integer page, SortEnum sort) {
        try (Connection connection = articleRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Pagination pagination = paginationService.getArticlePagination(page, sort);
                List<Article> articles = articleRepository.getLimitArticles(connection, pagination);
                List<ArticleDTO> articleDTOS = articles.stream()
                        .map(article -> {
                            ArticleDTO articleDTO = articleConverter.toDTO(article);
                            articleDTO.setComments(commentService.getCommentsByArticleId(article.getId()));
                            return articleDTO;
                        })
                        .collect(Collectors.toList());
                pagination.setEntities(articleDTOS);
                connection.commit();
                return pagination;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s. %s.", TRANSACTION_ERROR_MESSAGE, "When getting limit articles"), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s. %s.", SERVICE_ERROR_MESSAGE, "When getting limit articles"), e);
        }
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
    public void add(ArticleDTO articleDTO) {
        Article article = articleConverter.toEntity(articleDTO);
        articleRepository.create(article);
    }

    @Override
    @Transactional
    public void updateCountViews(Long id) {
        Article article = articleRepository.getById(id);
        article.setCountViews(article.getCountViews() + 1L);
        articleRepository.update(article);
    }
}
