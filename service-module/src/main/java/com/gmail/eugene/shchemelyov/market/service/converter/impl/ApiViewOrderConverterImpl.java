package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Order;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.OrderStatusEnum;
import com.gmail.eugene.shchemelyov.market.service.converter.ApiViewOrderConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ApiViewOrderDTO;
import org.springframework.stereotype.Component;

@Component
public class ApiViewOrderConverterImpl implements ApiViewOrderConverter {
    @Override
    public ApiViewOrderDTO toDTO(Order order) {
        ApiViewOrderDTO apiViewOrderDTO = new ApiViewOrderDTO();
        apiViewOrderDTO.setId(order.getId());
        apiViewOrderDTO.setUniqueNumber(order.getUniqueNumber());
        apiViewOrderDTO.setStatus(OrderStatusEnum.valueOf(order.getStatus()));
        if (order.getItems().iterator().hasNext()) {
            apiViewOrderDTO.setItemName(order.getItems().iterator().next().getName());
        }
        apiViewOrderDTO.setUserSurname(order.getUser().getSurname());
        apiViewOrderDTO.setUserPhone(order.getUser().getProfile().getPhone());
        apiViewOrderDTO.setCountItems(order.getCountItems());
        apiViewOrderDTO.setTotalPrice(order.getTotalPrice());
        return apiViewOrderDTO;
    }
}
