package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.OrderRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Order;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Order> getLimitOrders(Pagination pagination) {
        String query = "FROM " + entityClass.getName() + " ORDER BY date DESC";
        Query createdQuery = entityManager.createQuery(query)
                .setFirstResult(pagination.getStartLimitPosition())
                .setMaxResults(pagination.getLimitOnPage());
        return createdQuery.getResultList();
    }

    @Override
    public Integer getCountUserOrders(Long userId) {
        String query = "SELECT COUNT(O) FROM " + entityClass.getName() + " O WHERE O.user.id =: userId";
        Query createdQuery = entityManager.createQuery(query)
                .setParameter("userId", userId);
        return ((Number) createdQuery.getSingleResult()).intValue();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Order> getLimitUserOrders(Pagination pagination, Long userId) {
        String query = "FROM " + entityClass.getName() +
                " WHERE user.id =: userId" +
                " ORDER BY date DESC";
        Query createdQuery = entityManager.createQuery(query)
                .setFirstResult(pagination.getStartLimitPosition())
                .setMaxResults(pagination.getLimitOnPage())
                .setParameter("userId", userId);
        return createdQuery.getResultList();
    }

    @Override
    public Order getByUniqueNumber(Long uniqueNumber) {
        String query = "FROM " + entityClass.getName() + " WHERE uniqueNumber =: uniqueNumber";
        Query createdQuery = entityManager.createQuery(query)
                .setParameter("uniqueNumber", uniqueNumber);
        return (Order) createdQuery.getSingleResult();
    }
}
