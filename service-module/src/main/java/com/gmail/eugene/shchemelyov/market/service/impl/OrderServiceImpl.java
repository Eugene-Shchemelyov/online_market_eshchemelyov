package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.OrderRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Order;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.OrderStatusEnum;
import com.gmail.eugene.shchemelyov.market.service.OrderService;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.converter.ApiViewOrderConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.OrderConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.ViewOrderConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ApiViewOrderDTO;
import com.gmail.eugene.shchemelyov.market.service.model.OrderDTO;
import com.gmail.eugene.shchemelyov.market.service.model.ViewOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final PaginationService paginationService;
    private final OrderRepository orderRepository;
    private final ViewOrderConverter viewOrderConverter;
    private final OrderConverter orderConverter;
    private final ApiViewOrderConverter apiViewOrderConverter;

    @Autowired
    public OrderServiceImpl(
            PaginationService paginationService,
            OrderRepository orderRepository,
            ViewOrderConverter viewOrderConverter,
            OrderConverter orderConverter,
            ApiViewOrderConverter apiViewOrderConverter
    ) {
        this.paginationService = paginationService;
        this.orderRepository = orderRepository;
        this.viewOrderConverter = viewOrderConverter;
        this.orderConverter = orderConverter;
        this.apiViewOrderConverter = apiViewOrderConverter;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public Pagination getLimitOrders(Integer page) {
        Pagination pagination = paginationService.getOrderPagination(page);
        List<Order> orders = orderRepository.getLimitOrders(pagination);
        List<OrderDTO> convertedOrders = orders.stream()
                .map(orderConverter::toDTO)
                .collect(Collectors.toList());
        pagination.setEntities(convertedOrders);
        return pagination;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public Pagination getLimitUserOrders(Integer page, Long userId) {
        Pagination pagination = paginationService.getUserOrdersPagination(page, userId);
        List<Order> orders = orderRepository.getLimitUserOrders(pagination, userId);
        List<OrderDTO> convertedOrders = orders.stream()
                .map(orderConverter::toDTO)
                .collect(Collectors.toList());
        pagination.setEntities(convertedOrders);
        return pagination;
    }

    @Override
    @Transactional
    public ViewOrderDTO getByUniqueNumber(Long uniqueNumber) {
        Order order = orderRepository.getByUniqueNumber(uniqueNumber);
        return viewOrderConverter.toDTO(order);
    }

    @Override
    @Transactional
    public void updateStatusByUniqueNumber(Long uniqueNumber, OrderStatusEnum orderStatus) {
        Order order = orderRepository.getByUniqueNumber(uniqueNumber);
        order.setStatus(orderStatus.name());
        orderRepository.update(order);
    }

    @Override
    @Transactional
    public List<ApiViewOrderDTO> getOrders() {
        List<Order> orders = orderRepository.getAllEntities(false);
        return orders.stream()
                .map(apiViewOrderConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ApiViewOrderDTO getById(Long id) {
        Order order = orderRepository.getById(id);
        return apiViewOrderConverter.toDTO(order);
    }
}
