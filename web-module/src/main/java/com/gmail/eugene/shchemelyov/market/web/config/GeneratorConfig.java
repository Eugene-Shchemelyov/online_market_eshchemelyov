package com.gmail.eugene.shchemelyov.market.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class GeneratorConfig {
    @Bean
    public StringBuffer stringBuffer() {
        return new StringBuffer();
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
