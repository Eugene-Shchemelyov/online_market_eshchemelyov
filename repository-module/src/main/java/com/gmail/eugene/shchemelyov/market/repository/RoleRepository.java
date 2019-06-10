package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Role;

import java.util.List;

public interface RoleRepository extends GenericRepository<Long, Role> {
    List<Role> getAll();
}
