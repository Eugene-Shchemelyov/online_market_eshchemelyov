package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.ItemRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Item> getLimitItems(Pagination pagination, boolean isDeleted) {
        String query = "FROM " + entityClass.getName() +
                " WHERE isDeleted =: isDeleted" +
                " ORDER BY name ASC";
        Query createdQuery = entityManager.createQuery(query)
                .setFirstResult(pagination.getStartLimitPosition())
                .setMaxResults(pagination.getLimitOnPage())
                .setParameter("isDeleted", isDeleted);
        return createdQuery.getResultList();
    }

    @Override
    public int getCountByUniqueNumber(String uniqueNumber) {
        String query = "SELECT COUNT(*) FROM " + entityClass.getName() +
                " WHERE uniqueNumber =: uniqueNumber";
        Query createdQuery = entityManager.createQuery(query)
                .setParameter("uniqueNumber", uniqueNumber);
        return ((Number) createdQuery.getSingleResult()).intValue();
    }
}
