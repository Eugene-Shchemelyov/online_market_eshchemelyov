package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.OrderStatusEnum;
import com.gmail.eugene.shchemelyov.market.service.CreateOrderService;
import com.gmail.eugene.shchemelyov.market.service.OrderService;
import com.gmail.eugene.shchemelyov.market.service.model.AppUserPrincipal;
import com.gmail.eugene.shchemelyov.market.service.model.NewOrderDTO;
import com.gmail.eugene.shchemelyov.market.service.model.ViewOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final CreateOrderService createOrderService;

    @Autowired
    public OrderController(
            OrderService orderService,
            CreateOrderService createOrderService
    ) {
        this.orderService = orderService;
        this.createOrderService = createOrderService;
    }

    @GetMapping("/private/seller/orders")
    public String showOrdersPageForSeller(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) {
        Pagination pagination = orderService.getLimitOrders(page);
        model.addAttribute("pagination", pagination);
        return "order/all";
    }

    @GetMapping("/private/customer/orders")
    public String showOrdersPageForCustomer(
            Model model,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page
    ) {
        AppUserPrincipal appUserPrincipal =
                (AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = appUserPrincipal.getId();
        Pagination pagination = orderService.getLimitUserOrders(page, userId);
        model.addAttribute("pagination", pagination);
        return "order/all";
    }

    @GetMapping("/private/seller/orders/{uniqueNumber}")
    public String showOrderPageForSeller(
            Model model,
            @PathVariable("uniqueNumber") Long uniqueNumber
    ) {
        ViewOrderDTO viewOrderDTO = orderService.getByUniqueNumber(uniqueNumber);
        model.addAttribute("order", viewOrderDTO);
        return "order/current";
    }

    @PostMapping("/private/seller/orders/{uniqueNumber}/update")
    public String updateOrder(
            @PathVariable("uniqueNumber") Long uniqueNumber,
            @RequestParam("status") OrderStatusEnum orderStatus
    ) {
        orderService.updateStatusByUniqueNumber(uniqueNumber, orderStatus);
        return "redirect:/private/seller/orders";
    }

    @PostMapping("/private/customer/orders/new")
    public String createOrder(
            @Valid @ModelAttribute("order") NewOrderDTO newOrderDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "redirect:/private/items";
        }
        AppUserPrincipal appUserPrincipal =
                (AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = appUserPrincipal.getId();
        createOrderService.create(newOrderDTO, userId);
        return "redirect:/private/customer/orders";
    }
}
