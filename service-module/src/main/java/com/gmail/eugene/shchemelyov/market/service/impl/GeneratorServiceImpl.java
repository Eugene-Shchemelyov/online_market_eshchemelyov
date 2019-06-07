package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.IntStream;

import static com.gmail.eugene.shchemelyov.market.service.constant.GeneratorServiceConstant.ORDER_UNIQUE_NUMBER_MULTIPLIER;
import static com.gmail.eugene.shchemelyov.market.service.constant.GeneratorServiceConstant.SYMBOLS;

@Service
public class GeneratorServiceImpl implements GeneratorService {
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final Random random;

    @Autowired
    public GeneratorServiceImpl(
            PasswordEncoder passwordEncoder,
            JavaMailSender javaMailSender,
            Random random
    ) {
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.random = random;
    }

    @Override
    public String getRandomPassword(Integer passwordLength, String userEmail) {
        StringBuffer stringBuffer = new StringBuffer();
        IntStream.range(0, passwordLength).forEach(number ->
                stringBuffer.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length()))));
        String password = stringBuffer.toString();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Sending new password to user email");
        message.setText("New password: " + password);
        message.setTo("test199319931993@mail.ru");
        message.setFrom("Delivery");
        javaMailSender.send(message);
        return passwordEncoder.encode(password);
    }

    @Override
    public Long getRandomOrderNumber(Long userId) {
        return (userId * ORDER_UNIQUE_NUMBER_MULTIPLIER + random.nextInt(ORDER_UNIQUE_NUMBER_MULTIPLIER));
    }
}
