package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.exception.RepositoryException;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.repository.constant.ExceptionMessageConstant.REPOSITORY_ERROR_MESSAGE;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {
    private final static Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Autowired
    public UserRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public User loadUserByEmail(Connection connection, String email) {
        String query = "SELECT * FROM T_USER U" +
                " JOIN T_ROLE R ON U.F_ROLE_ID = R.F_ID" +
                " WHERE F_EMAIL = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return getUser(resultSet);
                }
                logger.error("{}: {}.", "User not found with email", email);
                throw new ExpectedException(String.format(
                        "%s: %s.", "User not found with email", email));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s. %s: %s.", REPOSITORY_ERROR_MESSAGE, "When loading the user with email", email), e);
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<User> getLimitUsers(Pagination pagination) {
        String query = "FROM " + entityClass.getName() + " ORDER BY F_EMAIL";
        Query createdQuery = entityManager.createQuery(query)
                .setFirstResult(pagination.getStartLimitPosition())
                .setMaxResults(pagination.getLimitOnPage());
        return createdQuery.getResultList();
    }

    @Override
    public Integer getCountUsersWithRole(Connection connection, String roleName, Boolean isDeleted) {
        String query = "SELECT COUNT(*) FROM T_USER U" +
                " JOIN T_ROLE R ON R.F_ID = U.F_ROLE_ID" +
                " WHERE R.F_NAME = ? AND U.F_IS_DELETED = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, roleName);
            ps.setBoolean(2, isDeleted);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                logger.error("You have problems with your database. Maybe tables not found.");
                throw new ExpectedException("You have problems with your database. Maybe tables not found.");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s. %s.", REPOSITORY_ERROR_MESSAGE, "When getting count user pages"), e);
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("U.F_ID"));
        user.setSurname(resultSet.getString("U.F_SURNAME"));
        user.setName(resultSet.getString("U.F_NAME"));
        user.setEmail(resultSet.getString("U.F_EMAIL"));
        user.setPassword(resultSet.getString("U.F_PASSWORD"));
        user.setRole(getRole(resultSet));
        user.setDeleted(resultSet.getBoolean("U.F_IS_DELETED"));
        return user;
    }

    private Role getRole(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("R.F_ID"));
        role.setName(resultSet.getString("R.F_NAME"));
        return role;
    }
}
