package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Review;

import java.util.List;

public interface ReviewRepository extends GenericRepository<Long, Review> {
    List<Review> getLimitReviews(Pagination pagination);
}
