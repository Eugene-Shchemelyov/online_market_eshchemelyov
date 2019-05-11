package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.PaginationRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

import static com.gmail.eugene.shchemelyov.market.repository.constant.PaginationConstant.LIMIT_ON_PAGE;
import static com.gmail.eugene.shchemelyov.market.repository.constant.UserConstant.DELETED;
import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.SERVICE_ERROR_MESSAGE;
import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.TRANSACTION_ERROR_MESSAGE;

@Service
public class PaginationServiceImpl implements PaginationService {
    private static final Logger logger = LoggerFactory.getLogger(PaginationServiceImpl.class);
    private final PaginationRepository paginationRepository;

    @Autowired
    public PaginationServiceImpl(PaginationRepository paginationRepository) {
        this.paginationRepository = paginationRepository;
    }

    @Override
    public Pagination getUserPagination(Integer page) {
        Integer countPages = getCountPages("T_USER");
        return getPagination(countPages, page);
    }

    @Override
    public Pagination getCommentPagination(Integer page) {
        Integer countPages = getCountPages("T_COMMENT");
        return getPagination(countPages, page);
    }

    private Integer getCountPages(String table) {
        try (Connection connection = paginationRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer countRaws = paginationRepository.getCountRaws(connection, table, DELETED);
                connection.commit();
                if (countRaws % LIMIT_ON_PAGE == 0 && countRaws != 0) {
                    return countRaws / LIMIT_ON_PAGE;
                } else {
                    return (countRaws + (LIMIT_ON_PAGE - countRaws % LIMIT_ON_PAGE)) / LIMIT_ON_PAGE;
                }
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s %s: %s", TRANSACTION_ERROR_MESSAGE, "When getting pagination for table", table), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s %s: %s", SERVICE_ERROR_MESSAGE, "When getting pagination for table", table), e);
        }
    }

    private Pagination getPagination(Integer countPages, Integer page) {
        Pagination pagination = new Pagination();
        if (page == null || page <= 0 || page > countPages) {
            page = 1;
        }
        pagination.setCurrentPage(page);
        pagination.setCountPages(countPages);
        return pagination;
    }
}
