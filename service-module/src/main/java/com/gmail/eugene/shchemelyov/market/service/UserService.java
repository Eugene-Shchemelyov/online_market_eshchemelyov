package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO loadUserByEmail(String email);

    UserDTO loadUserById(Long id);

    List<UserDTO> getUsers(Pagination pagination);

    Integer deleteUsersByEmail(List<String> emails);

    void update(UserDTO userDTO);

    void changePassword(Long id);

    void add(UserDTO userDTO);
}
