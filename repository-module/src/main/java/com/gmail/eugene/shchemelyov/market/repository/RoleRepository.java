package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleRepository extends GenericRepository {
    Role getRoleById(Connection connection, Long id);

    Role getRoleByName(Connection connection, String name);

    List<String> getAllRoles(Connection connection);

    String getRoleNameByUserId(Connection connection, Long id);

    String getRoleNameByUserEmail(Connection connection, String email);
}
