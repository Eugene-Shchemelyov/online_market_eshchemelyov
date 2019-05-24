package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Profile;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.model.AddUpdateUserDTO;

public interface ProfileConverter {
    Profile toEntity(AddUpdateUserDTO addUpdateUserDTO, User user);
}
