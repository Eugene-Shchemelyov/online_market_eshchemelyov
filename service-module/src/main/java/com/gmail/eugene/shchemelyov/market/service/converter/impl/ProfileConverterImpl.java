package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Profile;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.ProfileConverter;
import com.gmail.eugene.shchemelyov.market.service.model.ProfileDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class ProfileConverterImpl implements ProfileConverter {
    @Override
    public ProfileDTO toDTO(Profile profile, UserDTO userDTO) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUserDTO(userDTO);
        profileDTO.setAddress(profile.getAddress());
        profileDTO.setPhone(profile.getPhone());
        return profileDTO;
    }

    @Override
    public Profile toEntity(ProfileDTO profileDTO, User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setAddress(profileDTO.getAddress());
        profile.setPhone(profileDTO.getPhone());
        return profile;
    }
}
