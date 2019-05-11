package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.RoleRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.exception.RepositoryException;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
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
public class RoleRepositoryImpl extends GenericRepositoryImpl implements RoleRepository {
    private static final Logger logger = LoggerFactory.getLogger(RoleRepositoryImpl.class);

    @Autowired
    public RoleRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Role getRoleById(Connection connection, Long id) {
        String query = "SELECT * FROM T_ROLE WHERE F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return getRole(resultSet);
                }
                logger.error("{} {}", "Role not found with id:", id);
                throw new ExpectedException(String.format(
                        "%s %d", "Role not found with id:", id));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s %s %d.", REPOSITORY_ERROR_MESSAGE, "When get role with id:", id), e);
        }
    }

    @Override
    public Role getRoleByName(Connection connection, String name) {
        String query = "SELECT * FROM T_ROLE WHERE F_NAME = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return getRole(resultSet);
                }
                logger.error("{}: {}, {}.", "Role", name, "not found");
                throw new ExpectedException(String.format(
                        "%s: %s, %s.", "Role", name, "not found"));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s. %s: %s.", REPOSITORY_ERROR_MESSAGE, "When get role with name", name), e);
        }
    }

    @Override
    public List<String> getAllRoles(Connection connection) {
        String query = "SELECT F_NAME FROM T_ROLE";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet resultSet = ps.executeQuery()) {
            List<String> roles = new ArrayList<>();
            while (resultSet.next()) {
                roles.add(resultSet.getString("F_NAME"));
            }
            return roles;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s. %s.", REPOSITORY_ERROR_MESSAGE, "When getting roles"), e);
        }
    }

    @Override
    public String getRoleNameByUserEmail(Connection connection, String email) {
        String query = "SELECT R.F_NAME FROM T_ROLE R" +
                " JOIN T_USER U ON R.F_ID = U.F_ROLE_ID" +
                " WHERE U.F_EMAIL = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("F_NAME");
                }
                logger.error("{}: {}.", "User not found with email", email);
                throw new ExpectedException(String.format(
                        "%s: %s.", "User not found with email", email));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s. %s: %s.", REPOSITORY_ERROR_MESSAGE, "When loading the role by email", email), e);
        }
    }

    @Override
    public String getRoleNameByUserId(Connection connection, Long id) {
        String query = "SELECT R.F_NAME FROM T_ROLE R" +
                " JOIN T_USER U ON R.F_ID = U.F_ROLE_ID" +
                " WHERE U.F_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("F_NAME");
                }
                logger.error("{}: {}.", "User not found with id", id);
                throw new ExpectedException(String.format(
                        "%s: %d.", "User not found with id", id));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s. %s: %s.", REPOSITORY_ERROR_MESSAGE, "When loading the role with user id", id), e);
        }
    }

    private Role getRole(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("F_ID"));
        role.setName(resultSet.getString("F_NAME"));
        return role;
    }
}
