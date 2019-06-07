package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.service.model.NewOrderDTO;

public interface CreateOrderService {
    void create(NewOrderDTO newOrderDTO, Long userId);
}
