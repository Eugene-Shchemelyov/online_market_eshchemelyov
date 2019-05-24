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
    public User loadUserByEmail(String email) {
        String query = "FROM " + entityClass.getName() + " WHERE email =: email";
        Query createdQuery = entityManager.createQuery(query)
                .setParameter("email", email);
        return (User) createdQuery.getSingleResult();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<User> getLimitUsers(Pagination pagination) {
        String query = "FROM " + entityClass.getName() + " ORDER BY email";
        Query createdQuery = entityManager.createQuery(query)
                .setFirstResult(pagination.getStartLimitPosition())
                .setMaxResults(pagination.getLimitOnPage());
        return createdQuery.getResultList();
    }

    @Override
    public Integer getCountUsersWithRole(String roleName) {
        String query = "SELECT COUNT(U) FROM " + entityClass.getName() + " U WHERE U.role.name =: roleName";
        Query createdQuery = entityManager.createQuery(query)
                .setParameter("roleName", roleName);
        return ((Number) createdQuery.getSingleResult()).intValue();
    }

    @Override
    public Integer getCountUsersWithEmail(String email) {
        String query = "SELECT COUNT(U) FROM " + entityClass.getName() + " U WHERE U.email =: email";
        Query createdQuery = entityManager.createQuery(query)
                .setParameter("email", email);
        return ((Number) createdQuery.getSingleResult()).intValue();
    }
}
