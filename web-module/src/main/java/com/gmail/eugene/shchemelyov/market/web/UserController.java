package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.RoleService;
import com.gmail.eugene.shchemelyov.market.service.UserService;
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
    private final PaginationService paginationService;
    private final RoleService roleService;

    @Autowired
    public UserController(
            UserService userService,
            PaginationService paginationService,
            RoleService roleService
    ) {
        this.userService = userService;
        this.paginationService = paginationService;
        this.roleService = roleService;
    }

    @GetMapping("/private/administrator/users")
    public String getUsers(
            Model model,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "countDeletedUsers", required = false) Integer countDeletedUsers,
            @RequestParam(value = "update", required = false) Boolean update
    ) {
        Pagination pagination = paginationService.getUserPagination(page);
        List<UserDTO> users = userService.getUsers(pagination);
        model.addAttribute("pagination", pagination);
        model.addAttribute("users", users);
        model.addAttribute("countDeletedUsers", countDeletedUsers);
        model.addAttribute("update", update);
        return "user/all";
    }

    @PostMapping("/private/administrator/users/delete")
    public String deleteUsers(
            @RequestParam(value = "emails", required = false) List<String> emails
    ) {
        Integer countDeletedUsers = 0;
        if (emails != null) {
            countDeletedUsers = userService.deleteUsersByEmail(emails);
        }
        return "redirect:/private/administrator/users?countDeletedUsers=" + countDeletedUsers;
    }

    @GetMapping("/private/administrator/users/add")
    public String showPageAddUser(
            Model model
    ) {
        List<String> roles = roleService.getAllRoles();
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
            List<String> roles = roleService.getAllRoles();
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
        UserDTO userDTO = userService.loadUserById(id);
        List<String> roles = roleService.getAllRoles();
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
            List<String> roles = roleService.getAllRoles();
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
        userService.changePassword(id);
        return "redirect:/private/administrator/users/update?id=" + id + "&message=true";
    }
}
