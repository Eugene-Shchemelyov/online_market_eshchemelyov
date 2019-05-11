package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.ReviewRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.ReviewService;
import com.gmail.eugene.shchemelyov.market.service.converter.ReviewConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.UserConverter;
import com.gmail.eugene.shchemelyov.market.service.exception.ServiceException;
import com.gmail.eugene.shchemelyov.market.service.model.ReviewDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.SERVICE_ERROR_MESSAGE;
import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.TRANSACTION_ERROR_MESSAGE;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final static Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            ReviewConverter reviewConverter,
            UserRepository userRepository,
            UserConverter userConverter
    ) {
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public List<ReviewDTO> getReviews(Pagination pagination) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<ReviewDTO> convertedReviews = getConvertedReviews(connection, pagination);
                connection.commit();
                return convertedReviews;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s. %s.", TRANSACTION_ERROR_MESSAGE, "When getting limit reviews"), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s. %s.", SERVICE_ERROR_MESSAGE, "When getting limit reviews"), e);
        }
    }

    @Override
    public void changeReviewsDisplay(List<Long> reviewsId) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                reviewsId.forEach(id -> changeDisplay(connection, id));
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s. %s.", TRANSACTION_ERROR_MESSAGE, "When deleting reviews"), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s. %s.", SERVICE_ERROR_MESSAGE, "When deleting reviews"), e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = reviewRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                reviewRepository.deleteReviewById(connection, id, true);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s. %s: %d.", TRANSACTION_ERROR_MESSAGE, "When deleting review with id", id), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s. %s: %d.", SERVICE_ERROR_MESSAGE, "When deleting review with id", id), e);
        }
    }

    private List<ReviewDTO> getConvertedReviews(Connection connection, Pagination pagination) {
        Integer startLimitPosition = (pagination.getCurrentPage() - 1) * pagination.getLimitOnPage();
        pagination.setStartLimitPosition(startLimitPosition);
        List<Review> reviews = reviewRepository.getLimitReviews(connection, pagination);
        return reviews.stream()
                .map(review -> getReviewDTO(connection, review))
                .collect(Collectors.toList());
    }

    private ReviewDTO getReviewDTO(Connection connection, Review review) {
        Long userId = review.getUser().getId();
        User user = userRepository.loadUserById(connection, userId);
        UserDTO userDTO = userConverter.toUserDTO(user);
        return reviewConverter.toReviewDTO(review, userDTO);
    }

    private void changeDisplay(Connection connection, Long id) {
        Review review = reviewRepository.getReviewById(connection, id);
        reviewRepository.changeDisplay(connection, review);
    }
}
