package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.service.model.AddUpdateUserDTO;

public interface AddUpdateUserService {
    void update(AddUpdateUserDTO addUpdateUserDTO);

    void changePasswordById(Long id);

    AddUpdateUserDTO add(AddUpdateUserDTO addUpdateUserDTO);

    AddUpdateUserDTO getById(Long id);
}
