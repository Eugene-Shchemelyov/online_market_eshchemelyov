package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Profile;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.ProfileConverter;
import com.gmail.eugene.shchemelyov.market.service.model.UpdateUserDTO;
import org.springframework.stereotype.Component;

@Component
public class ProfileConverterImpl implements ProfileConverter {
    @Override
    public Profile toEntity(UpdateUserDTO updateUserDTO, User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setAddress(updateUserDTO.getAddress());
        profile.setPhone(updateUserDTO.getPhone());
        return profile;
    }
}
