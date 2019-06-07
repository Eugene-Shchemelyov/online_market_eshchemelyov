package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Order;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;

import java.util.List;

public interface OrderRepository extends GenericRepository<Long, Order> {
    List<Order> getLimitOrders(Pagination pagination);

    Integer getCountUserOrders(Long userId);

    List<Order> getLimitUserOrders(Pagination pagination, Long userId);

    Order getByUniqueNumber(Long uniqueNumber);
}
