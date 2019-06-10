package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.service.OrderService;
import com.gmail.eugene.shchemelyov.market.service.model.ApiViewOrderDTO;
import com.gmail.eugene.shchemelyov.market.service.model.ViewOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SECURE_REST_API;

@RestController
public class ApiOrderController extends ApiExceptionController {
    private final OrderService orderService;

    @Autowired
    public ApiOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping("/api/v1/orders")
    public List<ApiViewOrderDTO> showAllItems() {
        return orderService.getOrders();
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping("/api/v1/orders/{id}")
    public ApiViewOrderDTO showItemById(@PathVariable("id") Long id) {
        return orderService.getById(id);
    }
}
