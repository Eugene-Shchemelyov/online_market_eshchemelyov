package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;

public interface UserConverter {
    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
