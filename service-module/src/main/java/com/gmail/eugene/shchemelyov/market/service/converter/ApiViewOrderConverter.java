package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Order;
import com.gmail.eugene.shchemelyov.market.service.model.ApiViewOrderDTO;

public interface ApiViewOrderConverter {
    ApiViewOrderDTO toDTO(Order order);
}
