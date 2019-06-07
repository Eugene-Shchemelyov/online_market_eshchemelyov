package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.service.AddUpdateUserService;
import com.gmail.eugene.shchemelyov.market.service.model.AppUserPrincipal;
import com.gmail.eugene.shchemelyov.market.service.model.UpdateUserDTO;
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

import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.PASSWORD_NOT_VALID_MESSAGE;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.PASSWORD_PATTERN;

@Controller
public class ProfileController {
    private final PasswordEncoder passwordEncoder;
    private final AddUpdateUserService addUpdateUserService;

    @Autowired
    public ProfileController(
            PasswordEncoder passwordEncoder,
            AddUpdateUserService addUpdateUserService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.addUpdateUserService = addUpdateUserService;
    }

    @GetMapping("/private/profile")
    public String getProfilePage(Model model) {
        AppUserPrincipal appUserPrincipal =
                (AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = appUserPrincipal.getId();
        UpdateUserDTO updateUserDTO = addUpdateUserService.getById(userId);
        model.addAttribute("user", updateUserDTO);
        return "profile";
    }

    @PostMapping("/private/profile/update")
    public String updateProfile(
            @RequestParam(value = "oldPassword", required = false) String oldPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @Valid @ModelAttribute("user") UpdateUserDTO updateUserDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors() || updateUserDTO.getPassword() != null) {
            return "profile";
        }
        AppUserPrincipal appUserPrincipal =
                (AppUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(oldPassword.trim().isEmpty() && newPassword.trim().isEmpty())) {
            if (newPassword.matches(PASSWORD_PATTERN) &&
                    passwordEncoder.matches(oldPassword, appUserPrincipal.getPassword())) {
                updateUserDTO.setPassword(passwordEncoder.encode(newPassword));
            } else {
                bindingResult.rejectValue("password", "", PASSWORD_NOT_VALID_MESSAGE);
                return "profile";
            }
        }
        Long userId = appUserPrincipal.getId();
        addUpdateUserService.updateById(updateUserDTO, userId);
        return "redirect:/private/profile";
    }
}