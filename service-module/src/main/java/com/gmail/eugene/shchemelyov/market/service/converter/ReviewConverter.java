package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.model.ReviewDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;

public interface ReviewConverter {
    Review toReview(ReviewDTO reviewDTO, User user);

    ReviewDTO toReviewDTO(Review review, UserDTO userDTO);
}
