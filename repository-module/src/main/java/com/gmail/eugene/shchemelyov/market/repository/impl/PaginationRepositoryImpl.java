package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.PaginationRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.gmail.eugene.shchemelyov.market.repository.constant.ExceptionMessageConstant.REPOSITORY_ERROR_MESSAGE;

@Repository
public class PaginationRepositoryImpl extends GenericRepositoryImpl implements PaginationRepository {
    private static final Logger logger = LoggerFactory.getLogger(PaginationRepositoryImpl.class);

    @Autowired
    public PaginationRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Integer getCountRaws(Connection connection, String table, Boolean isDeleted) {
        String query = "SELECT COUNT(*) FROM " + table + " WHERE F_DELETED = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, isDeleted);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                logger.error("{}: {}.", "Table not found. Maybe you delete the table", table);
                throw new ExpectedException(String.format(
                        "%s: %s.", "Table not found. Maybe you delete the table", table));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s %s: %s", REPOSITORY_ERROR_MESSAGE, "When getting count raws from table", table), e);
        }
    }
}
