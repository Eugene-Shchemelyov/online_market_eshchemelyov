package com.gmail.eugene.shchemelyov.market.service.model;

import com.gmail.eugene.shchemelyov.market.repository.model.enums.OrderStatusEnum;

import java.math.BigDecimal;

public class OrderDTO {
    private Long uniqueNumber;
    private OrderStatusEnum status;
    private String itemName;
    private Integer countItems;
    private BigDecimal totalPrice;

    public Long getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(Long uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getCountItems() {
        return countItems;
    }

    public void setCountItems(Integer countItems) {
        this.countItems = countItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
