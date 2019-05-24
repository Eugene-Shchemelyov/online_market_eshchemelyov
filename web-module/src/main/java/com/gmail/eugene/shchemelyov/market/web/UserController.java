package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.AddUpdateUserService;
import com.gmail.eugene.shchemelyov.market.service.RoleService;
import com.gmail.eugene.shchemelyov.market.service.UserService;
import com.gmail.eugene.shchemelyov.market.service.model.AddUpdateUserDTO;
import com.gmail.eugene.shchemelyov.market.service.model.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final AddUpdateUserService addUpdateUserService;
    private final RoleService roleService;

    @Autowired
    public UserController(
            UserService userService,
            AddUpdateUserService addUpdateUserService,
            RoleService roleService
    ) {
        this.userService = userService;
        this.addUpdateUserService = addUpdateUserService;
        this.roleService = roleService;
    }

    @GetMapping("/private/users")
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

    @PostMapping("/private/users/delete")
    public String deleteUsers(
            @RequestParam(value = "usersIds", required = false) List<Long> usersIds
    ) {
        if (usersIds != null) {
            userService.deleteUsersById(usersIds);
        }
        return "redirect:/private/users";
    }

    @GetMapping("/private/users/new")
    public String showPageAddUser(Model model) {
        List<RoleDTO> roles = roleService.getAllRoles();
        model.addAttribute("user", new AddUpdateUserDTO());
        model.addAttribute("roles", roles);
        return "user/new";
    }

    @PostMapping("/private/users/new")
    public String addUser(
            Model model,
            @Valid @ModelAttribute("user") AddUpdateUserDTO addUpdateUserDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<RoleDTO> roles = roleService.getAllRoles();
            model.addAttribute("roles", roles);
            return "user/new";
        }
        addUpdateUserService.add(addUpdateUserDTO);
        return "redirect:/private/users";
    }

    @GetMapping("/private/users/{id}/update")
    public String showPageUpdateUser(
            Model model,
            @RequestParam(value = "message", required = false) Boolean message,
            @PathVariable(value = "id") Long id
    ) {
        AddUpdateUserDTO addUpdateUserDTO = addUpdateUserService.getById(id);
        List<RoleDTO> roles = roleService.getAllRoles();
        model.addAttribute("user", addUpdateUserDTO);
        model.addAttribute("roles", roles);
        model.addAttribute("message", message);
        return "user/update";
    }

    @PostMapping("/private/users/{id}/update")
    public String updateUser(
            Model model,
            @PathVariable(value = "id") Long id,
            @Valid @ModelAttribute("user") AddUpdateUserDTO addUpdateUserDTO,
            BindingResult bindingResult
    ) {
        addUpdateUserDTO.setId(id);
        if (bindingResult.hasErrors()) {
            List<RoleDTO> roles = roleService.getAllRoles();
            model.addAttribute("roles", roles);
            return "user/update";
        }
        addUpdateUserService.update(addUpdateUserDTO);
        return "redirect:/private/users?update=true";
    }

    @PostMapping("/private/users/{id}/password")
    public String sendMessageToChangePassword(
            @PathVariable(value = "id") Long id
    ) {
        addUpdateUserService.changePasswordById(id);
        return "redirect:/private/users/" + id + "/update?message=true";
    }
}
