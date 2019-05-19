package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.service.model.ReviewDTO;

public interface ReviewConverter {
    ReviewDTO toDTO(Review review);

    Review toEntity(ReviewDTO reviewDTO);
}
