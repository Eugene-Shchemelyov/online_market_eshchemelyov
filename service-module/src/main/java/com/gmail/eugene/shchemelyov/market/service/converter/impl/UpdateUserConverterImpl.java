package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.ProfileConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.UpdateUserConverter;
import com.gmail.eugene.shchemelyov.market.service.model.UpdateUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserConverterImpl implements UpdateUserConverter {
    private final ProfileConverter profileConverter;

    @Autowired
    public UpdateUserConverterImpl(ProfileConverter profileConverter) {
        this.profileConverter = profileConverter;
    }

    @Override
    public UpdateUserDTO toDTO(User user) {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setName(user.getName());
        updateUserDTO.setSurname(user.getSurname());
        updateUserDTO.setAddress(user.getProfile().getAddress());
        updateUserDTO.setPhone(user.getProfile().getPhone());
        return updateUserDTO;
    }

    @Override
    public User toEntity(UpdateUserDTO updateUserDTO) {
        User user = new User();
        user.setName(updateUserDTO.getName());
        user.setSurname(updateUserDTO.getSurname());
        user.setPassword(updateUserDTO.getPassword());
        user.setProfile(profileConverter.toEntity(updateUserDTO, user));
        return user;
    }
}
