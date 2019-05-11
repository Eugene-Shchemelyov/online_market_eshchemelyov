package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.RoleRepository;
import com.gmail.eugene.shchemelyov.market.service.RoleService;
import com.gmail.eugene.shchemelyov.market.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.SERVICE_ERROR_MESSAGE;
import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.TRANSACTION_ERROR_MESSAGE;

@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<String> getAllRoles() {
        try (Connection connection = roleRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<String> roles = roleRepository.getAllRoles(connection);
                connection.commit();
                return roles;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s. %s.", TRANSACTION_ERROR_MESSAGE, "When getting roles"), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s. %s.", SERVICE_ERROR_MESSAGE, "When getting roles"), e);
        }
    }
}

