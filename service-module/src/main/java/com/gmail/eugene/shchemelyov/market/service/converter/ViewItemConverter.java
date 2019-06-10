package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.service.model.ViewItemDTO;

public interface ViewItemConverter {
    ViewItemDTO toDTO(Item item);
}
