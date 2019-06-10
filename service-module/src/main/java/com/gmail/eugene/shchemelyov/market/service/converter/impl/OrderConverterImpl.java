package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Order;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.OrderStatusEnum;
import com.gmail.eugene.shchemelyov.market.service.converter.OrderConverter;
import com.gmail.eugene.shchemelyov.market.service.model.OrderDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderConverterImpl implements OrderConverter {
    @Override
    public OrderDTO toDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUniqueNumber(order.getUniqueNumber());
        orderDTO.setStatus(OrderStatusEnum.valueOf(order.getStatus()));
        if (order.getItems().iterator().hasNext()) {
            orderDTO.setItemName(order.getItems().iterator().next().getName());
        }
        orderDTO.setCountItems(order.getCountItems());
        orderDTO.setTotalPrice(order.getTotalPrice());
        return orderDTO;
    }
}
