package com.gmail.eugene.shchemelyov.market.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
