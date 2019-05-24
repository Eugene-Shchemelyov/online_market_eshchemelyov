package com.gmail.eugene.shchemelyov.market.repository;

import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;

import java.util.List;

public interface ItemRepository extends GenericRepository<Long, Item> {
    List<Item> getLimitItems(Pagination pagination);
}
