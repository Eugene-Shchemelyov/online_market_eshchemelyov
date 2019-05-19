package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.service.UserService;
import com.gmail.eugene.shchemelyov.market.service.model.AppUserPrincipal;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class ProfileController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public ProfileController(
            PasswordEncoder passwordEncoder,
            UserService userService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping("/private/profile")
    public String getProfilePage(
            Model model
    ) {
        AppUserPrincipal appUserPrincipal =
                (AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = appUserPrincipal.getId();
        UserDTO userDTO = userService.getById(userId);
        model.addAttribute("user", userDTO);
        return "profile";
    }

    @PostMapping("/private/profile/update")
    public String updateProfile(
            Model model,
            @RequestParam(value = "oldPassword", required = false) String oldPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @Valid @ModelAttribute("user") UserDTO userDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        AppUserPrincipal appUserPrincipal =
                (AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(oldPassword.trim().isEmpty() && newPassword.trim().isEmpty())) {
            if (newPassword.matches("[a-zA-Z0-9]{8,30}") &&
                    passwordEncoder.matches(oldPassword, appUserPrincipal.getPassword())) {
                userDTO.setPassword(passwordEncoder.encode(newPassword));
            } else {
                model.addAttribute("passwordError", true);
                return "profile";
            }
        }
        userService.update(userDTO);
        return "redirect:/private/profile";
    }
}