package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.service.GeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.IntStream;

import static com.gmail.eugene.shchemelyov.market.service.constant.GeneratorServiceConstant.SYMBOLS;

@Service
public class GeneratorServiceImpl implements GeneratorService {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final Random random;

    @Autowired
    public GeneratorServiceImpl(
            PasswordEncoder passwordEncoder,
            Random random
    ) {
        this.passwordEncoder = passwordEncoder;
        this.random = random;
    }

    @Override
    public String getRandomPassword(Integer length) {
        StringBuffer stringBuffer = new StringBuffer();
        IntStream.range(0, length).forEach(number ->
                stringBuffer.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length()))));
        String password = stringBuffer.toString();
        logger.info(password);
        return passwordEncoder.encode(password);
    }
}
