package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.service.converter.ReviewConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ReviewDTO;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverterImpl implements ReviewConverter {
    @Override
    public ReviewDTO toDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setUserPatronymic(review.getUser().getPatronymic());
        reviewDTO.setUserName(review.getUser().getName());
        reviewDTO.setUserSurname(review.getUser().getSurname());
        reviewDTO.setText(review.getText());
        reviewDTO.setDate(review.getDate());
        reviewDTO.setDisplay(review.isDisplay());
        return reviewDTO;
    }
}
