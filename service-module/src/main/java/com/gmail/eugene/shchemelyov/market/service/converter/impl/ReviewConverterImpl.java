package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.ReviewConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ReviewDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverterImpl implements ReviewConverter {
    @Override
    public Review toReview(ReviewDTO reviewDTO, User user) {
        Review review = new Review();
        review.setId(reviewDTO.getId());
        review.setUser(user);
        review.setText(reviewDTO.getText());
        review.setDate(reviewDTO.getDate());
        review.setDisplay(reviewDTO.isDisplay());
        review.setDeleted(reviewDTO.isDeleted());
        return review;
    }

    @Override
    public ReviewDTO toReviewDTO(Review review, UserDTO userDTO) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setUser(userDTO);
        reviewDTO.setText(review.getText());
        reviewDTO.setDate(review.getDate());
        reviewDTO.setDisplay(review.isDisplay());
        reviewDTO.setDeleted(review.isDeleted());
        return reviewDTO;
    }
}
