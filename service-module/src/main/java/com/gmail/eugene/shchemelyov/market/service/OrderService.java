package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.OrderStatusEnum;
import com.gmail.eugene.shchemelyov.market.service.model.ApiViewOrderDTO;
import com.gmail.eugene.shchemelyov.market.service.model.NewOrderDTO;
import com.gmail.eugene.shchemelyov.market.service.model.ViewOrderDTO;

import java.util.List;

public interface OrderService {
    Pagination getLimitOrders(Integer page);

    Pagination getLimitUserOrders(Integer page, Long userId);

    ViewOrderDTO getByUniqueNumber(Long uniqueNumber);

    void updateStatusByUniqueNumber(Long uniqueNumber, OrderStatusEnum orderStatus);

    List<ApiViewOrderDTO> getOrders();

    ApiViewOrderDTO getById(Long id);
}
