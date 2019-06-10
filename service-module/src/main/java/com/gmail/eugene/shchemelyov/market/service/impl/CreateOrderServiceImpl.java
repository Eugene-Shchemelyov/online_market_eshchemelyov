package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.ItemRepository;
import com.gmail.eugene.shchemelyov.market.repository.OrderRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Item;
import com.gmail.eugene.shchemelyov.market.repository.model.Order;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.OrderStatusEnum;
import com.gmail.eugene.shchemelyov.market.service.CreateOrderService;
import com.gmail.eugene.shchemelyov.market.service.GeneratorService;
import com.gmail.eugene.shchemelyov.market.service.model.NewOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static com.gmail.eugene.shchemelyov.market.service.constant.DateFormatConstant.DATABASE_DATE_FORMAT;

@Service
public class CreateOrderServiceImpl implements CreateOrderService {
    private final ItemRepository itemRepository;
    private final GeneratorService generatorService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public CreateOrderServiceImpl(
            ItemRepository itemRepository,
            GeneratorService generatorService,
            UserRepository userRepository,
            OrderRepository orderRepository
    ) {
        this.itemRepository = itemRepository;
        this.generatorService = generatorService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void create(NewOrderDTO newOrderDTO, Long userId) {
        Order order = createOrder(newOrderDTO, userId);
        orderRepository.create(order);
    }

    private Order createOrder(NewOrderDTO newOrderDTO, Long userId) {
        Order order = new Order();
        order.setStatus(OrderStatusEnum.NEW.name());

        Integer countItems = newOrderDTO.getCountItems();
        order.setCountItems(countItems);

        String date = new SimpleDateFormat(DATABASE_DATE_FORMAT)
                .format(new Date().getTime());
        order.setDate(date);

        Item item = itemRepository.getById(newOrderDTO.getItemId());
        order.setItems(Collections.singletonList(item));

        BigDecimal totalPrice = item.getPrice().multiply(BigDecimal.valueOf(countItems));
        order.setTotalPrice(totalPrice);

        Long uniqueNumber = generatorService.getRandomOrderNumber(userId);
        order.setUniqueNumber(uniqueNumber);

        User user = userRepository.getById(userId);
        order.setUser(user);
        return order;
    }
}
