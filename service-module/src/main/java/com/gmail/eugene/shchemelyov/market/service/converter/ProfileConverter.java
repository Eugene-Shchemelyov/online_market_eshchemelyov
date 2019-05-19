package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Profile;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.model.ProfileDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;

public interface ProfileConverter {
    ProfileDTO toDTO(Profile profile, UserDTO userDTO);

    Profile toEntity(ProfileDTO profileDTO, User user);
}
