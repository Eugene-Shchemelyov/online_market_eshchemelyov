package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.service.AddUpdateUserService;
import com.gmail.eugene.shchemelyov.market.service.model.AddUpdateUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SECURE_REST_API;

@RestController
public class ApiUserController {
    private final AddUpdateUserService addUpdateUserService;

    @Autowired
    public ApiUserController(AddUpdateUserService addUpdateUserService) {
        this.addUpdateUserService = addUpdateUserService;
    }

    @Secured(value = {SECURE_REST_API})
    @PostMapping(value = "/api/v1/users", consumes = "application/json")
    public AddUpdateUserDTO addUserDTO(@Valid @RequestBody AddUpdateUserDTO addUpdateUserDTO) {
        return addUpdateUserService.add(addUpdateUserDTO);
    }
}
