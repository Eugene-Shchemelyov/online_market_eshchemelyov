package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;

public interface PaginationService {
    Pagination getUserPagination(Integer page);

    Pagination getReviewPagination(Integer page);

    Pagination getArticlePagination(Integer page, SortEnum sort);
}
