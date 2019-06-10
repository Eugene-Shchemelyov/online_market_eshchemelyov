package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Profile;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.model.UpdateUserDTO;

public interface ProfileConverter {
    Profile toEntity(UpdateUserDTO updateUserDTO, User user);
}
