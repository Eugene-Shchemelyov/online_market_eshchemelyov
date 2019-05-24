package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.Profile;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.ProfileConverter;
import com.gmail.eugene.shchemelyov.market.service.model.AddUpdateUserDTO;
import org.springframework.stereotype.Component;

@Component
public class ProfileConverterImpl implements ProfileConverter {
    @Override
    public Profile toEntity(AddUpdateUserDTO addUpdateUserDTO, User user) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setAddress(addUpdateUserDTO.getAddress());
        profile.setPhone(addUpdateUserDTO.getPhone());
        return profile;
    }
}
