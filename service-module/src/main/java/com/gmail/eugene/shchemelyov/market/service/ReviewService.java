package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.model.ReviewDTO;

import java.util.List;

public interface ReviewService {
    Pagination getLimitReviews(Integer page);

    void changeReviewsDisplay(List<Long> reviewsId);

    void delete(Long id);
}
