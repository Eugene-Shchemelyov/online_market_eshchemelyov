package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.GenericRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class GenericRepositoryImpl implements GenericRepository {
    private static Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);
    private final DataSource dataSource;

    @Autowired
    public GenericRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException("Problems with get connection", e);
        }
    }
}
