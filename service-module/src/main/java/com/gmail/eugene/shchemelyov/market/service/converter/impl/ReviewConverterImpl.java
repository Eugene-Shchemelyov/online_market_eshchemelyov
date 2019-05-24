package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.service.converter.ReviewConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.UserConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverterImpl implements ReviewConverter {
    private UserConverter userConverter;

    @Autowired
    public ReviewConverterImpl(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public ReviewDTO toDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setUser(userConverter.toDTO(review.getUser()));
        reviewDTO.setText(review.getText());
        reviewDTO.setDate(review.getDate());
        reviewDTO.setDisplay(review.isDisplay());
        return reviewDTO;
    }

    @Override
    public Review toEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setUser(userConverter.toEntity(reviewDTO.getUser()));
        review.setText(reviewDTO.getText());
        review.setDate(reviewDTO.getDate());
        review.setDisplay(reviewDTO.isDisplay());
        return review;
    }
}
