package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.CommentRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.exception.RepositoryException;
import com.gmail.eugene.shchemelyov.market.repository.model.Comment;
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
public class CommentRepositoryImpl extends GenericRepositoryImpl implements CommentRepository {
    private static final Logger logger = LoggerFactory.getLogger(CommentRepositoryImpl.class);

    @Autowired
    public CommentRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Comment> getLimitComments(Connection connection, Pagination pagination) {
        String query = "SELECT * FROM T_COMMENT WHERE F_DELETED = ? ORDER BY F_DATE DESC LIMIT ?, ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, pagination.isDeleted());
            ps.setInt(2, pagination.getStartLimitPosition());
            ps.setInt(3, pagination.getLimitOnPage());
            try (ResultSet resultSet = ps.executeQuery()) {
                List<Comment> comments = new ArrayList<>();
                while (resultSet.next()) {
                    comments.add(getComment(resultSet));
                }
                return new ArrayList<>(comments);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s %s.", REPOSITORY_ERROR_MESSAGE, "When getting limit comments"), e);
        }
    }

    @Override
    public void deleteCommentById(Connection connection, Long id, Boolean isDeleted) {
        String query = "UPDATE T_COMMENT SET F_DELETED = ? WHERE F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, isDeleted);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s %s: %d.", REPOSITORY_ERROR_MESSAGE, "When deleting comment with id", id), e);
        }
    }

    @Override
    public Comment getCommentById(Connection connection, Long id) {
        String query = "SELECT * FROM T_COMMENT WHERE F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return getComment(resultSet);
                }
                logger.error("{}: {} {}", "Comment with id", id, "isn't found");
                throw new ExpectedException(String.format("%s: %d %s", "Comment with id", id, "isn't found"));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s %s.", REPOSITORY_ERROR_MESSAGE, "When getting limit comments"), e);
        }
    }

    @Override
    public void changeDisplay(Connection connection, Comment comment) {
        String query = "UPDATE T_COMMENT SET F_DISPLAY = ? WHERE F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, !comment.isDisplay());
            ps.setLong(2, comment.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("%s %s: %d.",
                    REPOSITORY_ERROR_MESSAGE, "When changing display comment with id", comment.getId()), e);
        }
    }

    private Comment getComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getLong("F_ID"));
        comment.setUser(getUser(resultSet.getLong("F_USER_ID")));
        comment.setText(resultSet.getString("F_TEXT"));
        comment.setDate(resultSet.getTimestamp("F_DATE").toString());
        comment.setDisplay(resultSet.getBoolean("F_DISPLAY"));
        comment.setDeleted(resultSet.getBoolean("F_DELETED"));
        return comment;
    }

    private User getUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
