package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.User;

import java.util.List;

public interface UserRepository extends GenericRepository<Long, User> {
    User loadUserByEmail(String email, boolean isDeleted);

    List<User> getLimitUsers(Pagination pagination, boolean isDeleted);

    Integer getCountUsersWithRole(String roleName, boolean isDeleted);

    Integer getCountUsersWithEmail(String email, boolean isDeleted);
}
