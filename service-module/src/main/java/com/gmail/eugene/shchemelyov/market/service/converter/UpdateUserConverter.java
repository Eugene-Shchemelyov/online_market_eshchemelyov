package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.model.UpdateUserDTO;

public interface UpdateUserConverter {
    UpdateUserDTO toDTO(User user);

    User toEntity(UpdateUserDTO updateUserDTO);
}
