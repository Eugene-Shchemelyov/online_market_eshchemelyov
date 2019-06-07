package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.service.AddUpdateUserService;
import com.gmail.eugene.shchemelyov.market.service.RoleService;
import com.gmail.eugene.shchemelyov.market.service.model.RoleDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SECURE_REST_API;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ApiUserController extends ApiExceptionController {
    private final AddUpdateUserService addUpdateUserService;
    private final RoleService roleService;

    @Autowired
    public ApiUserController(
            AddUpdateUserService addUpdateUserService,
            RoleService roleService
    ) {
        this.addUpdateUserService = addUpdateUserService;
        this.roleService = roleService;
    }

    @Secured(value = {SECURE_REST_API})
    @PostMapping(value = "/api/v1/users", consumes = APPLICATION_JSON_VALUE)
    public UserDTO addUser(@Valid @RequestBody UserDTO userDTO) {
        return addUpdateUserService.add(userDTO);
    }

    @Secured(value = {SECURE_REST_API})
    @GetMapping(value = "/api/v1/roles", consumes = APPLICATION_JSON_VALUE)
    public List<RoleDTO> getRoles() {
        return roleService.getAllRoles();
    }
}
