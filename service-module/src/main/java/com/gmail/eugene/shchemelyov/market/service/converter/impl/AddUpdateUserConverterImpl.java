package com.gmail.eugene.shchemelyov.market.service.converter.impl;

import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.AddUpdateUserConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.ProfileConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.RoleConverter;
import com.gmail.eugene.shchemelyov.market.service.model.AddUpdateUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddUpdateUserConverterImpl implements AddUpdateUserConverter {
    private final RoleConverter roleConverter;
    private final ProfileConverter profileConverter;

    @Autowired
    public AddUpdateUserConverterImpl(
            RoleConverter roleConverter,
            ProfileConverter profileConverter
    ) {
        this.roleConverter = roleConverter;
        this.profileConverter = profileConverter;
    }

    @Override
    public AddUpdateUserDTO toDTO(User user) {
        AddUpdateUserDTO addUpdateUserDTO = new AddUpdateUserDTO();
        addUpdateUserDTO.setId(user.getId());
        addUpdateUserDTO.setName(user.getName());
        addUpdateUserDTO.setSurname(user.getSurname());
        addUpdateUserDTO.setEmail(user.getEmail());
        addUpdateUserDTO.setRole(roleConverter.toDTO(user.getRole()));
        addUpdateUserDTO.setAddress(user.getProfile().getAddress());
        addUpdateUserDTO.setPhone(user.getProfile().getPhone());
        return addUpdateUserDTO;
    }

    @Override
    public User toEntity(AddUpdateUserDTO addUpdateUserDTO) {
        User user = new User();
        user.setId(addUpdateUserDTO.getId());
        user.setName(addUpdateUserDTO.getName());
        user.setSurname(addUpdateUserDTO.getSurname());
        user.setEmail(addUpdateUserDTO.getEmail());
        user.setPassword(addUpdateUserDTO.getPassword());
        user.setRole(roleConverter.toEntity(addUpdateUserDTO.getRole()));
        user.setProfile(profileConverter.toEntity(addUpdateUserDTO, user));
        return user;
    }
}
