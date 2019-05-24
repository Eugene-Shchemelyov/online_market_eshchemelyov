package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.model.ItemDTO;

import java.util.List;

public interface ItemService {
    Pagination getLimitItems(Integer page);

    ItemDTO getById(Long id);

    void copyById(Long id);

    void deleteById(Long id);

    List<ItemDTO> getItems();

    ItemDTO add(ItemDTO itemDTO);
}
