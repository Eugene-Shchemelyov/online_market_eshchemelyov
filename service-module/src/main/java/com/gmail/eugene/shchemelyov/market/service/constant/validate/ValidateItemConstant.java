package com.gmail.eugene.shchemelyov.market.service.constant.validate;

public class ValidateItemConstant {
    public static final String NAME_PATTERN = "[a-zA-Z0-9.,!?:;\\s]{0,100}";
    public static final String UNIQUE_NUMBER_PATTERN =
            "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";
    public static final String DESCRIPTION_PATTERN = "[a-zA-Z0-9.,!?:;\\s]{0,200}";
    public static final int COUNT_INTEGERS = 8;
    public static final int COUNT_FRACTIONS = 2;
}
