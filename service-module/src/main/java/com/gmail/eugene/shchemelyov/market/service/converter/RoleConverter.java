package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.service.model.RoleDTO;

public interface RoleConverter {
    RoleDTO toDTO(Role role);

    Role toEntity(RoleDTO roleDTO);
}
