package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.ReviewRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {
    @Autowired
    public ReviewRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Review> getLimitReviews(Pagination pagination) {
        String query = "FROM " + entityClass.getName() + " ORDER BY F_DATE DESC";
        Query createdQuery = entityManager.createQuery(query)
                .setFirstResult(pagination.getStartLimitPosition())
                .setMaxResults(pagination.getLimitOnPage());
        return createdQuery.getResultList();
    }
}
