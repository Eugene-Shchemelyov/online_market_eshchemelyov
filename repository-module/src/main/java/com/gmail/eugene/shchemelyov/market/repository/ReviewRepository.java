package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;

import java.sql.Connection;
import java.util.List;

public interface ReviewRepository extends GenericRepository<Long, Review> {
    List<Review> getLimitReviews(Pagination pagination);
}
