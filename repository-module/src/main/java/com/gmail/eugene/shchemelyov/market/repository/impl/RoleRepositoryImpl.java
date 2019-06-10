package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.RoleRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long, Role> implements RoleRepository {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Role> getAll() {
        String query = "FROM " + entityClass.getName();
        Query createdQuery = entityManager.createQuery(query);
        return createdQuery.getResultList();
    }
}
