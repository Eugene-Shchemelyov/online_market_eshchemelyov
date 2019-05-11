package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;

import java.sql.Connection;
import java.util.List;

public interface ReviewRepository extends GenericRepository {
    List<Review> getLimitReviews(Connection connection, Pagination pagination);

    void deleteReviewById(Connection connection, Long id, Boolean isDeleted);

    Review getReviewById(Connection connection, Long id);

    void changeDisplay(Connection connection, Review review);
}
