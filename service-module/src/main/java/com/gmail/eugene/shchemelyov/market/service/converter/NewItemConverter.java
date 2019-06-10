package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.service.model.NewItemDTO;

public interface NewItemConverter {
    Item toEntity(NewItemDTO newItemDTO);
}
