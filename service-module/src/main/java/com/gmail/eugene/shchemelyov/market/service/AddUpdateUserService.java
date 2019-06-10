package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.service.model.UpdateUserDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;

public interface AddUpdateUserService {
    void updateById(UpdateUserDTO updateUserDTO, Long userId);

    void changePasswordById(Long id);

    UserDTO add(UserDTO userDTO);

    UpdateUserDTO getById(Long id);

    void changeRoleById(Long userId, Long roleId);
}
