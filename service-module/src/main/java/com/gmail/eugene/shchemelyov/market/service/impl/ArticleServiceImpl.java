package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.ArticleRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.ArticleService;
import com.gmail.eugene.shchemelyov.market.service.converter.UpdateArticleConverter;
import com.gmail.eugene.shchemelyov.market.service.exception.ServiceException;
import com.gmail.eugene.shchemelyov.market.service.model.NewArticleDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UpdateArticleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.gmail.eugene.shchemelyov.market.service.constant.DateFormatConstant.DATABASE_DATE_FORMAT;
import static com.gmail.eugene.shchemelyov.market.service.constant.DateFormatConstant.WEB_DATE_FORMAT;

@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final UpdateArticleConverter updateArticleConverter;

    @Autowired
    public ArticleServiceImpl(
            ArticleRepository articleRepository,
            UserRepository userRepository,
            UpdateArticleConverter updateArticleConverter
    ) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.updateArticleConverter = updateArticleConverter;
    }

    @Override
    @Transactional
    public UpdateArticleDTO getById(Long id) {
        Article article = articleRepository.getById(id);
        return updateArticleConverter.toDTO(article);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public NewArticleDTO add(NewArticleDTO newArticleDTO, Long userId) {
        Article article = getArticle(newArticleDTO);
        User user = userRepository.getById(userId);
        article.setUser(user);
        articleRepository.create(article);
        return newArticleDTO;
    }

    @Override
    @Transactional
    public void incrementCountViews(Long id) {
        Article article = articleRepository.getById(id);
        Long incrementedCountViews = article.getCountViews() + 1L;
        article.setCountViews(incrementedCountViews);
        articleRepository.update(article);
    }

    @Override
    @Transactional
    public void update(UpdateArticleDTO updateArticleDTO) {
        Article article = articleRepository.getById(updateArticleDTO.getId());
        article.setName(updateArticleDTO.getName());
        article.setText(updateArticleDTO.getText());
        String date = new SimpleDateFormat(DATABASE_DATE_FORMAT)
                .format(new Date().getTime());
        article.setDate(date);
        articleRepository.update(article);
    }

    private Article getArticle(NewArticleDTO newArticleDTO) {
        Article article = new Article();
        article.setDate(getConvertedDate(newArticleDTO.getDate()));
        article.setName(newArticleDTO.getName());
        article.setText(newArticleDTO.getText());
        return article;
    }

    private String getConvertedDate(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(WEB_DATE_FORMAT, Locale.getDefault());
            Date converterDate = simpleDateFormat.parse(date);
            return new SimpleDateFormat(DATABASE_DATE_FORMAT)
                    .format(converterDate);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format("%s: %s.", "Bad date format when converting the date", date), e);
        }
    }
}
