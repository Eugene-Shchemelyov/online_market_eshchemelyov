package com.gmail.eugene.shchemelyov.market.repository.model.enums;

public enum SortEnum {
    DATE_DESC("date descending"),
    DATE_ASC("date ascending"),
    VIEWS_DESC("views descending"),
    VIEWS_ASC("views ascending"),
    USER_SURNAME_DESC("surname descending"),
    USER_SURNAME_ASC("surname ascending");

    private String name;

    SortEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
