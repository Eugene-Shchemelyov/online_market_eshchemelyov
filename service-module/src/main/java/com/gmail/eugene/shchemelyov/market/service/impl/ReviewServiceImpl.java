package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.ReviewRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.ReviewService;
import com.gmail.eugene.shchemelyov.market.service.converter.ReviewConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final PaginationService paginationService;

    @Autowired
    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            ReviewConverter reviewConverter,
            PaginationService paginationService
    ) {
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
        this.paginationService = paginationService;
    }

    @Override
    @Transactional
    public Pagination getLimitReviews(Integer page) {
        Pagination pagination = paginationService.getReviewPagination(page);
        List<Review> reviews = reviewRepository.getLimitReviews(pagination);
        List<ReviewDTO> reviewDTOS = reviews.stream()
                .map(reviewConverter::toDTO)
                .collect(Collectors.toList());
        pagination.setEntities(reviewDTOS);
        return pagination;
    }

    @Override
    @Transactional
    public void changeReviewsDisplay(List<Long> reviewsId) {
        for (Long id : reviewsId) {
            Review review = reviewRepository.getById(id);
            review.setDisplay(!review.isDisplay());
            reviewRepository.update(review);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }
}
