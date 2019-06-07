package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {
    @Override
    public User loadUserByEmail(String email, boolean isDeleted) {
        String query = "FROM " + entityClass.getName() +
                " WHERE email =: email" +
                " AND isDeleted =: isDeleted";
        Query createdQuery = entityManager.createQuery(query)
                .setParameter("email", email)
                .setParameter("isDeleted", isDeleted);
        return (User) createdQuery.getSingleResult();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<User> getLimitUsers(Pagination pagination, boolean isDeleted) {
        String query = "FROM " + entityClass.getName() +
                " WHERE isDeleted =: isDeleted" +
                " ORDER BY email";
        Query createdQuery = entityManager.createQuery(query)
                .setFirstResult(pagination.getStartLimitPosition())
                .setMaxResults(pagination.getLimitOnPage())
                .setParameter("isDeleted", isDeleted);
        return createdQuery.getResultList();
    }

    @Override
    public Integer getCountUsersWithRole(String roleName, boolean isDeleted) {
        String query = "SELECT COUNT(U) FROM " + entityClass.getName() +
                " U WHERE U.role.name =: roleName" +
                " AND U.isDeleted =: isDeleted";
        Query createdQuery = entityManager.createQuery(query)
                .setParameter("roleName", roleName)
                .setParameter("isDeleted", isDeleted);
        return ((Number) createdQuery.getSingleResult()).intValue();
    }

    @Override
    public Integer getCountUsersWithEmail(String email, boolean isDeleted) {
        String query = "SELECT COUNT(U) FROM " + entityClass.getName() +
                " U WHERE U.email =: email" +
                " AND U.isDeleted =: isDeleted";
        Query createdQuery = entityManager.createQuery(query)
                .setParameter("email", email)
                .setParameter("isDeleted", isDeleted);
        return ((Number) createdQuery.getSingleResult()).intValue();
    }
}
