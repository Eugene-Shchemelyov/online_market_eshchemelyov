package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Order;
import com.gmail.eugene.shchemelyov.market.service.model.ViewOrderDTO;

public interface ViewOrderConverter {
    ViewOrderDTO toDTO(Order order);
}
