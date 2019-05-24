package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.ArticleRepository;
import com.gmail.eugene.shchemelyov.market.repository.ItemRepository;
import com.gmail.eugene.shchemelyov.market.repository.ReviewRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.gmail.eugene.shchemelyov.market.repository.constant.PaginationConstant.LIMIT_ON_PAGE;

@Service
public class PaginationServiceImpl implements PaginationService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ArticleRepository articleRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public PaginationServiceImpl(
            UserRepository userRepository,
            ReviewRepository reviewRepository,
            ArticleRepository articleRepository,
            ItemRepository itemRepository
    ) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.articleRepository = articleRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Pagination getUserPagination(Integer page) {
        Integer countEntities = userRepository.getCountOfEntities();
        return getPagination(countEntities, page);
    }

    @Override
    public Pagination getReviewPagination(Integer page) {
        Integer countEntities = reviewRepository.getCountOfEntities();
        return getPagination(countEntities, page);
    }

    @Override
    public Pagination getArticlePagination(Integer page, SortEnum sort) {
        Integer countEntities = articleRepository.getCountOfEntities();
        Pagination pagination = getPagination(countEntities, page);
        pagination.setSort(sort);
        return pagination;
    }

    @Override
    public Pagination getItemPagination(Integer page) {
        Integer countEntities = itemRepository.getCountOfEntities();
        return getPagination(countEntities, page);
    }

    private Pagination getPagination(Integer countEntities, Integer page) {
        Integer countPages;
        if (countEntities % LIMIT_ON_PAGE == 0 && countEntities != 0) {
            countPages = countEntities / LIMIT_ON_PAGE;
        } else {
            countPages = (countEntities + (LIMIT_ON_PAGE - countEntities % LIMIT_ON_PAGE)) / LIMIT_ON_PAGE;
        }
        Pagination pagination = new Pagination();
        if (page == null || page <= 0 || page > countPages) {
            page = 1;
        }
        pagination.setCurrentPage(page);
        pagination.setCountPages(countPages);
        Integer startLimitPosition = (pagination.getCurrentPage() - 1) * pagination.getLimitOnPage();
        pagination.setStartLimitPosition(startLimitPosition);
        return pagination;
    }
}
