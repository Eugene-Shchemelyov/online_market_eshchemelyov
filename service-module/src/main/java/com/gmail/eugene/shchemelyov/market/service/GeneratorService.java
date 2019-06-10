package com.gmail.eugene.shchemelyov.market.service;

public interface GeneratorService {
    String getRandomPassword(Integer passwordLength, String userEmail);

    Long getRandomOrderNumber(Long userId);
}
