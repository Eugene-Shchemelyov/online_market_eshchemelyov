package com.gmail.eugene.shchemelyov.market.repository;

import java.sql.Connection;

public interface GenericRepository {
    Connection getConnection();
}
