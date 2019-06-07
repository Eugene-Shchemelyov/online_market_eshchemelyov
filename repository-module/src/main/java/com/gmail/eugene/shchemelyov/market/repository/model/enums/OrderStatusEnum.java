package com.gmail.eugene.shchemelyov.market.repository.model.enums;

public enum OrderStatusEnum {
    NEW("New"),
    IN_PROGRESS("In progress"),
    DELIVERED("Delivered"),
    REJECTED("Rejected");

    private String name;

    OrderStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
