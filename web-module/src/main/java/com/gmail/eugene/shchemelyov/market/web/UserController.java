package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.RoleService;
import com.gmail.eugene.shchemelyov.market.service.UserService;
import com.gmail.eugene.shchemelyov.market.service.model.RoleDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(
            UserService userService,
            RoleService roleService
    ) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/private/administrator/users")
    public String getUsers(
            Model model,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "update", required = false) Boolean update
    ) {
        Pagination pagination = userService.getLimitUsers(page);
        model.addAttribute("pagination", pagination);
        model.addAttribute("update", update);
        return "user/all";
    }

    @PostMapping("/private/administrator/users/delete")
    public String deleteUsers(
            @RequestParam(value = "usersIds", required = false) List<Long> usersIds
    ) {
        if (usersIds != null) {
            userService.deleteUsersById(usersIds);
        }
        return "redirect:/private/administrator/users";
    }

    @GetMapping("/private/administrator/users/add")
    public String showPageAddUser(Model model) {
        List<RoleDTO> roles = roleService.getAllRoles();
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", roles);
        return "user/add";
    }

    @PostMapping("/private/administrator/users/add")
    public String addUser(
            Model model,
            @Valid @ModelAttribute("user") UserDTO userDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<RoleDTO> roles = roleService.getAllRoles();
            model.addAttribute("roles", roles);
            return "user/add";
        }
        userService.add(userDTO);
        return "redirect:/private/administrator/users";
    }

    @GetMapping("/private/administrator/users/update")
    public String showPageUpdateUser(
            Model model,
            @RequestParam(value = "message", required = false) Boolean message,
            @RequestParam(value = "id") Long id
    ) {
        UserDTO userDTO = userService.getById(id);
        List<RoleDTO> roles = roleService.getAllRoles();
        model.addAttribute("user", userDTO);
        model.addAttribute("roles", roles);
        model.addAttribute("message", message);
        return "user/update";
    }

    @PostMapping("/private/administrator/users/update")
    public String updateUser(
            Model model,
            @RequestParam(value = "id") Long id,
            @Valid @ModelAttribute("user") UserDTO userDTO,
            BindingResult bindingResult
    ) {
        userDTO.setId(id);
        if (bindingResult.hasErrors()) {
            List<RoleDTO> roles = roleService.getAllRoles();
            model.addAttribute("roles", roles);
            return "user/update";
        }
        userService.update(userDTO);
        return "redirect:/private/administrator/users?update=true";
    }

    @PostMapping("/private/administrator/users/message")
    public String sendMessageToChangePassword(
            @RequestParam(value = "id") Long id
    ) {
        userService.changePasswordById(id);
        return "redirect:/private/administrator/users/update?id=" + id + "&message=true";
    }
}
