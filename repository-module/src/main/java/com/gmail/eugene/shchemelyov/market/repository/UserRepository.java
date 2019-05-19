package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.User;

import java.sql.Connection;
import java.util.List;

public interface UserRepository extends GenericRepository<Long, User> {
    User loadUserByEmail(Connection connection, String email);

    List<User> getLimitUsers(Pagination pagination);

    Integer getCountUsersWithRole(Connection connection, String roleName, Boolean isDeleted);
}
