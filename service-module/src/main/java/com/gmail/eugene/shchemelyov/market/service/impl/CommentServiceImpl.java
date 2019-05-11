package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.CommentRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Comment;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.CommentService;
import com.gmail.eugene.shchemelyov.market.service.converter.CommentConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.UserConverter;
import com.gmail.eugene.shchemelyov.market.service.exception.ServiceException;
import com.gmail.eugene.shchemelyov.market.service.model.CommentDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.eugene.shchemelyov.market.repository.constant.UserConstant.DELETED;
import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.SERVICE_ERROR_MESSAGE;
import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.TRANSACTION_ERROR_MESSAGE;

@Service
public class CommentServiceImpl implements CommentService {
    private final static Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public CommentServiceImpl(
            CommentRepository commentRepository,
            CommentConverter commentConverter,
            UserRepository userRepository,
            UserConverter userConverter
    ) {
        this.commentRepository = commentRepository;
        this.commentConverter = commentConverter;
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public List<CommentDTO> getComments(Pagination pagination) {
        try (Connection connection = commentRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<CommentDTO> convertedComments = getConvertedComments(connection, pagination);
                connection.commit();
                return convertedComments;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s %s.", TRANSACTION_ERROR_MESSAGE, "When getting limit comments"), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s %s.", SERVICE_ERROR_MESSAGE, "When getting limit comments"), e);
        }
    }

    @Override
    public void changeCommentsDisplay(List<Long> commentsId) {
        try (Connection connection = commentRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                commentsId.forEach(id -> changeDisplay(connection, id));
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s %s.", TRANSACTION_ERROR_MESSAGE, "When deleting comments"), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s %s.", SERVICE_ERROR_MESSAGE, "When deleting comments"), e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = commentRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                commentRepository.deleteCommentById(connection, id, !DELETED);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s %s: %d.", TRANSACTION_ERROR_MESSAGE, "When deleting comment with id", id), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s %s: %d.", SERVICE_ERROR_MESSAGE, "When deleting comment with id", id), e);
        }
    }

    private List<CommentDTO> getConvertedComments(Connection connection, Pagination pagination) {
        Integer startLimitPosition = (pagination.getCurrentPage() - 1) * pagination.getLimitOnPage();
        pagination.setStartLimitPosition(startLimitPosition);
        List<Comment> comments = commentRepository.getLimitComments(connection, pagination);
        return comments.stream()
                .map(comment -> getCommentDTO(connection, comment))
                .collect(Collectors.toList());
    }

    private CommentDTO getCommentDTO(Connection connection, Comment comment) {
        Long userId = comment.getUser().getId();
        User user = userRepository.loadUserById(connection, userId);
        UserDTO userDTO = userConverter.toUserDTO(user);
        return commentConverter.toCommentDTO(comment, userDTO);
    }

    private void changeDisplay(Connection connection, Long id) {
        Comment comment = commentRepository.getCommentById(connection, id);
        commentRepository.changeDisplay(connection, comment);
    }
}
