package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserByEmail(String email);

    UserDTO getById(Long id);

    Pagination getLimitUsers(Integer page);

    void deleteUsersById(List<Long> usersIds);
}
