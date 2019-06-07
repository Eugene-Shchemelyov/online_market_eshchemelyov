package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Order;
import com.gmail.eugene.shchemelyov.market.service.model.OrderDTO;

public interface OrderConverter {
    OrderDTO toDTO(Order order);
}
