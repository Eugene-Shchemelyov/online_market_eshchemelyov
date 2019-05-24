package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.service.model.ItemDTO;

public interface ItemConverter {
    ItemDTO toDTO(Item item);

    Item toEntity(ItemDTO itemDTO);
}
