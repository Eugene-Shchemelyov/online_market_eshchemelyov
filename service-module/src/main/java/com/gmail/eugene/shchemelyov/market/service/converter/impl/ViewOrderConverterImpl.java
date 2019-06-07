package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Order;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.OrderStatusEnum;
import com.gmail.eugene.shchemelyov.market.service.converter.ViewOrderConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ViewOrderDTO;
import org.springframework.stereotype.Component;

@Component
public class ViewOrderConverterImpl implements ViewOrderConverter {
    @Override
    public ViewOrderDTO toDTO(Order order) {
        ViewOrderDTO viewOrderDTO = new ViewOrderDTO();
        viewOrderDTO.setUniqueNumber(order.getUniqueNumber());
        viewOrderDTO.setStatus(OrderStatusEnum.valueOf(order.getStatus()));
        if (order.getItems().iterator().hasNext()) {
            viewOrderDTO.setItemName(order.getItems().iterator().next().getName());
        }
        viewOrderDTO.setUserSurname(order.getUser().getSurname());
        viewOrderDTO.setUserPhone(order.getUser().getProfile().getPhone());
        viewOrderDTO.setCountItems(order.getCountItems());
        viewOrderDTO.setTotalPrice(order.getTotalPrice());
        return viewOrderDTO;
    }
}
