package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.RoleRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.AddUpdateUserService;
import com.gmail.eugene.shchemelyov.market.service.GeneratorService;
import com.gmail.eugene.shchemelyov.market.service.converter.UpdateUserConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.UserConverter;
import com.gmail.eugene.shchemelyov.market.service.model.UpdateUserDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gmail.eugene.shchemelyov.market.service.constant.GeneratorServiceConstant.PASSWORD_LENGTH;
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.COUNT_ADMINISTRATORS;

@Service
public class AddUpdateUserServiceImpl implements AddUpdateUserService {
    private static final Logger logger = LoggerFactory.getLogger(AddUpdateUserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GeneratorService generatorService;
    private final UpdateUserConverter updateUserConverter;
    private final UserConverter userConverter;

    @Autowired
    public AddUpdateUserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            GeneratorService generatorService,
            UpdateUserConverter updateUserConverter,
            UserConverter userConverter
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.generatorService = generatorService;
        this.updateUserConverter = updateUserConverter;
        this.userConverter = userConverter;
    }

    @Override
    @Transactional
    public void updateById(UpdateUserDTO updateUserDTO, Long userId) {
        User user = userRepository.getById(userId);
        if (updateUserDTO.getName() != null) {
            user.setName(updateUserDTO.getName());
        }
        if (updateUserDTO.getSurname() != null) {
            user.setSurname(updateUserDTO.getSurname());
        }
        if (updateUserDTO.getPassword() != null) {
            user.setPassword(updateUserDTO.getPassword());
        }
        if (updateUserDTO.getAddress() != null) {
            user.getProfile().setAddress(updateUserDTO.getAddress());
        }
        if (updateUserDTO.getPhone() != null) {
            user.getProfile().setPhone(updateUserDTO.getPhone());
        }
        userRepository.update(user);
    }

    @Override
    @Transactional
    public void changePasswordById(Long id) {
        User user = userRepository.getById(id);
        user.setPassword(generatorService.getRandomPassword(PASSWORD_LENGTH, user.getEmail()));
        userRepository.update(user);
    }

    @Override
    @Transactional
    public UserDTO add(UserDTO userDTO) {
        User user = userConverter.toEntity(userDTO);
        String email = userDTO.getEmail();
        if (userRepository.getCountUsersWithEmail(email) == 0) {
            if (user.getPassword() == null) {
                user.setPassword(generatorService.getRandomPassword(PASSWORD_LENGTH, userDTO.getEmail()));
            }
            userRepository.create(user);
        } else {
            logger.error("User exists with email: {}", email);
            throw new ExpectedException(String.format(
                    "%s: %s.", "User exists with email", email));
        }
        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public UpdateUserDTO getById(Long id) {
        User user = userRepository.getById(id);
        return updateUserConverter.toDTO(user);
    }

    @Override
    @Transactional
    public void changeRoleById(Long userId, Long roleId) {
        User user = userRepository.getById(userId);
        Role role = roleRepository.getById(roleId);
        if (user.getRole().getName().equals(ADMINISTRATOR) &&
                !role.getName().equals(ADMINISTRATOR) &&
                (userRepository.getCountUsersWithRole(ADMINISTRATOR) <= COUNT_ADMINISTRATORS)) {
            logger.error("You can't lower privileges to the last administrator.");
            throw new ExpectedException("You can't lower privileges to the last administrator.");
        } else {
            user.setRole(role);
            userRepository.update(user);
        }
    }
}
