package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.model.AddUpdateUserDTO;

public interface AddUpdateUserConverter {
    AddUpdateUserDTO toDTO(User user);

    User toEntity(AddUpdateUserDTO addUpdateUserDTO);
}
