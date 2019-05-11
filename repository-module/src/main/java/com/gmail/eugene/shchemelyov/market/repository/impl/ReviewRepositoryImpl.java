package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.ReviewRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.exception.RepositoryException;
import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.repository.constant.ExceptionMessageConstant.REPOSITORY_ERROR_MESSAGE;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl implements ReviewRepository {
    private static final Logger logger = LoggerFactory.getLogger(ReviewRepositoryImpl.class);

    @Autowired
    public ReviewRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Review> getLimitReviews(Connection connection, Pagination pagination) {
        String query = "SELECT * FROM T_REVIEW WHERE F_DELETED = ? ORDER BY F_DATE DESC LIMIT ?, ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, pagination.isDeleted());
            ps.setInt(2, pagination.getStartLimitPosition());
            ps.setInt(3, pagination.getLimitOnPage());
            try (ResultSet resultSet = ps.executeQuery()) {
                List<Review> reviews = new ArrayList<>();
                while (resultSet.next()) {
                    reviews.add(getReview(resultSet));
                }
                return new ArrayList<>(reviews);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s %s.", REPOSITORY_ERROR_MESSAGE, "When getting limit reviews"), e);
        }
    }

    @Override
    public void deleteReviewById(Connection connection, Long id, Boolean isDeleted) {
        String query = "UPDATE T_REVIEW SET F_DELETED = ? WHERE F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, isDeleted);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s %s: %d.", REPOSITORY_ERROR_MESSAGE, "When deleting review with id", id), e);
        }
    }

    @Override
    public Review getReviewById(Connection connection, Long id) {
        String query = "SELECT * FROM T_REVIEW WHERE F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return getReview(resultSet);
                }
                logger.error("{}: {} {}", "Review with id", id, "isn't found");
                throw new ExpectedException(String.format("%s: %d %s", "Review with id", id, "isn't found"));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s %s.", REPOSITORY_ERROR_MESSAGE, "When getting limit reviews"), e);
        }
    }

    @Override
    public void changeDisplay(Connection connection, Review review) {
        String query = "UPDATE T_REVIEW SET F_DISPLAY = ? WHERE F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, !review.isDisplay());
            ps.setLong(2, review.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("%s %s: %d.",
                    REPOSITORY_ERROR_MESSAGE, "When changing display review with id", review.getId()), e);
        }
    }

    private Review getReview(ResultSet resultSet) throws SQLException {
        Review review = new Review();
        review.setId(resultSet.getLong("F_ID"));
        review.setUser(getUser(resultSet.getLong("F_USER_ID")));
        review.setText(resultSet.getString("F_TEXT"));
        review.setDate(resultSet.getTimestamp("F_DATE").toString());
        review.setDisplay(resultSet.getBoolean("F_DISPLAY"));
        review.setDeleted(resultSet.getBoolean("F_DELETED"));
        return review;
    }

    private User getUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
