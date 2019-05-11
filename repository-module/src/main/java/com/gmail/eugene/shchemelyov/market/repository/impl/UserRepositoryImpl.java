package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.exception.RepositoryException;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserRepositoryImpl extends GenericRepositoryImpl implements UserRepository {
    private final static Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    public UserRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public User loadUserByEmail(Connection connection, String email) {
        String query = "SELECT * FROM T_USER WHERE F_EMAIL = ?";
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
    public User loadUserById(Connection connection, Long id) {
        String query = "SELECT * FROM T_USER WHERE F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return getUser(resultSet);
                }
                logger.error("{}: {}.", "User not found with id", id);
                throw new ExpectedException(String.format(
                        "%s: %d.", "User not found with id", id));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s. %s: %s.", REPOSITORY_ERROR_MESSAGE, "When loading the user with email", id), e);
        }
    }

    @Override
    public List<User> getLimitUsers(Connection connection, Pagination pagination) {
        String query = "SELECT * FROM T_USER WHERE F_DELETED = ? ORDER BY F_EMAIL LIMIT ?, ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, pagination.isDeleted());
            ps.setInt(2, pagination.getStartLimitPosition());
            ps.setInt(3, pagination.getLimitOnPage());
            try (ResultSet resultSet = ps.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (resultSet.next()) {
                    users.add(getUser(resultSet));
                }
                return users;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s. %s.", REPOSITORY_ERROR_MESSAGE, "When getting limit users"), e);
        }
    }

    @Override
    public void add(Connection connection, User user) {
        String query = "INSERT INTO T_USER (F_SURNAME, F_NAME, F_PATRONYMIC, F_EMAIL, F_PASSWORD," +
                " F_ROLE_ID, F_DELETED) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getSurname());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPatronymic());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPassword());
            ps.setLong(6, user.getRole().getId());
            ps.setBoolean(7, user.isDeleted());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ExpectedException(String.format("%s. %s. %s: %s.", REPOSITORY_ERROR_MESSAGE,
                    "When adding the user", "Maybe user exists with this email", user.getEmail()), e);
        }
    }

    @Override
    public Integer getCountUsersWithRole(Connection connection, String roleName, Boolean isDeleted) {
        String query = "SELECT COUNT(*) FROM T_USER U" +
                " JOIN T_ROLE R ON R.F_ID = U.F_ROLE_ID" +
                " WHERE R.F_NAME = ? AND U.F_DELETED = ?";
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

    @Override
    public Integer deleteByEmail(Connection connection, String email, Boolean isDeleted) {
        String query = "UPDATE T_USER SET F_DELETED = ? WHERE F_EMAIL = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, isDeleted);
            ps.setString(2, email);
            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s. %s: %s.", REPOSITORY_ERROR_MESSAGE, "When deleting the user with email", email), e);
        }
    }

    @Override
    public void updatePassword(Connection connection, User user) {
        String query = "UPDATE T_USER SET F_PASSWORD = ? WHERE F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getPassword());
            ps.setLong(2, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("%s. %s: %d.",
                    REPOSITORY_ERROR_MESSAGE, "When changing the password of the user with id", user.getId()), e);
        }
    }

    @Override
    public void update(Connection connection, User user) {
        String query = "UPDATE T_USER SET F_SURNAME = ?, F_NAME = ?, F_PATRONYMIC = ?, F_ROLE_ID = ?" +
                " WHERE F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getSurname());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPatronymic());
            ps.setLong(4, user.getRole().getId());
            ps.setLong(5, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("%s. %s: %d.",
                    REPOSITORY_ERROR_MESSAGE, "When updating the user with id", user.getId()), e);
        }
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("F_ID"));
        user.setSurname(resultSet.getString("F_SURNAME"));
        user.setName(resultSet.getString("F_NAME"));
        user.setPatronymic(resultSet.getString("F_PATRONYMIC"));
        user.setEmail(resultSet.getString("F_EMAIL"));
        user.setPassword(resultSet.getString("F_PASSWORD"));
        user.setRole(getRole(resultSet.getLong("F_ROLE_ID")));
        user.setDeleted(resultSet.getBoolean("F_DELETED"));
        return user;
    }

    private Role getRole(Long id) {
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
