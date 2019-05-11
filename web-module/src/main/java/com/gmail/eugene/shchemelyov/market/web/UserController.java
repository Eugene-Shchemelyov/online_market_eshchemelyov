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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping(value = {"/private/users", "/private/users/{page}"})
    public String getUsers(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable(required = false) Integer page
    ) {
        Pagination pagination = paginationService.getUserPagination(page);
        List<UserDTO> users = userService.getUsers(pagination);
        model.addAttribute("pagination", pagination);
        model.addAttribute("users", users);
        String numberDeletedUsers = httpServletRequest.getParameter("countDeletedUsers");
        if (numberDeletedUsers != null) {
            model.addAttribute("countDeletedUsers", Integer.parseInt(numberDeletedUsers));
        }
        String update = httpServletRequest.getParameter("update");
        if (update != null) {
            model.addAttribute("update", Boolean.parseBoolean(update));
        }
        return "user/all";
    }

    @PostMapping("/private/users/delete")
    public String deleteUsers(
            @RequestParam(value = "emails", required = false) List<String> emails
    ) {
        Integer countDeletedUsers = 0;
        if (emails != null) {
            countDeletedUsers = userService.deleteUsersByEmail(emails);
        }
        return "redirect:/private/users?countDeletedUsers=" + countDeletedUsers;
    }

    @GetMapping("/private/users/add")
    public String showPageAddUser(
            Model model
    ) {
        List<String> roles = roleService.getAllRoles();
        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", roles);
        return "user/add";
    }

    @PostMapping("/private/users/add")
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
        return "redirect:/private/users";
    }

    @GetMapping("/private/users/{id}/update")
    public String showPageUpdateUser(
            Model model,
            HttpServletRequest httpServletRequest,
            @PathVariable("id") Long id
    ) {
        UserDTO userDTO = userService.loadUserById(id);
        List<String> roles = roleService.getAllRoles();
        model.addAttribute("user", userDTO);
        model.addAttribute("roles", roles);
        String message = httpServletRequest.getParameter("message");
        if (message != null) {
            model.addAttribute("message", Boolean.parseBoolean(message));
        }
        return "user/update";
    }

    @PostMapping("/private/users/{id}/update")
    public String updateUser(
            Model model,
            @PathVariable("id") Long id,
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
        return "redirect:/private/users?update=true";
    }

    @PostMapping("/private/users/{id}/message")
    public String sendMessageToChangePassword(
            @PathVariable("id") Long id
    ) {
        userService.changePassword(id);
        return "redirect:/private/users/" + id + "/update?message=true";
    }
}
