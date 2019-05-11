package com.gmail.eugene.shchemelyov.market.repository;

import java.sql.Connection;

public interface PaginationRepository extends GenericRepository {
    Integer getCountRaws(Connection connection, String table, Boolean isDeleted);
}
