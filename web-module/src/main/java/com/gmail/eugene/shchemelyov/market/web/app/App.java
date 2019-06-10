package com.gmail.eugene.shchemelyov.market.web.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = "com.gmail.eugene.shchemelyov.market",
        exclude= UserDetailsServiceAutoConfiguration.class
)
@EntityScan(basePackages = "com.gmail.eugene.shchemelyov.market.repository.model")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
