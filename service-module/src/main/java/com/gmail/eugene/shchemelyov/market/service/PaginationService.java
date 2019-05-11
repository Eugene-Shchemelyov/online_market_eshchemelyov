package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;

public interface PaginationService {
    Pagination getUserPagination(Integer page);

    Pagination getReviewPagination(Integer page);
}
