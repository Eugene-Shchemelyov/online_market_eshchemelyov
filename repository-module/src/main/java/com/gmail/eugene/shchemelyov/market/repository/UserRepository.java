package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.User;

import java.sql.Connection;
import java.util.List;

public interface UserRepository extends GenericRepository {
    User loadUserByEmail(Connection connection, String email);

    User loadUserById(Connection connection, Long id);

    List<User> getLimitUsers(Connection connection, Pagination pagination);

    Integer getCountUsersWithRole(Connection connection, String roleName, Boolean isDeleted);

    Integer deleteByEmail(Connection connection, String email, Boolean isDeleted);

    void updatePassword(Connection connection, User user);

    void update(Connection connection, User user);

    void add(Connection connection, User user);
}
