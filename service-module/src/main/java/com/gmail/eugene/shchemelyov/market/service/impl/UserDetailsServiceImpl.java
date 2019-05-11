package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.service.UserService;
import com.gmail.eugene.shchemelyov.market.service.model.AppUserPrincipal;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserDTO userDTO = userService.loadUserByEmail(email);
        if (userDTO == null) {
            throw new UsernameNotFoundException(String.format("%s %s %s", "User", email, "isn't found."));
        }
        return new AppUserPrincipal(userDTO);
    }
}
