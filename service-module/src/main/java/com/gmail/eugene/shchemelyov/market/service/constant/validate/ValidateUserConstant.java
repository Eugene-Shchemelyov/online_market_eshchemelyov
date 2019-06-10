package com.gmail.eugene.shchemelyov.market.service.constant.validate;

public class ValidateUserConstant {
    public static final String PATRONYMIC_PATTERN = "[a-zA-Z]{2,40}";
    public static final String NAME_PATTERN = "[a-zA-Z]{2,20}";
    public static final String SURNAME_PATTERN = "[a-zA-Z]{2,40}";
    public static final String PASSWORD_PATTERN = "[a-zA-Z0-9]{8,30}";
    public static final String ADDRESS_PATTERN = "[a-zA-Z0-9.,!?\\s]{0,100}";
    public static final String PHONE_PATTERN = "\\+[0-9]{0,15}";
    public static final int MAX_EMAIL_SIZE = 50;
    public static final String PASSWORD_NOT_VALID_MESSAGE =
            "Password error or incorrect validation: Only latin characters and numbers, min 8, max 30";
}
