package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.RoleRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.AddUpdateUserService;
import com.gmail.eugene.shchemelyov.market.service.GeneratorService;
import com.gmail.eugene.shchemelyov.market.service.converter.AddUpdateUserConverter;
import com.gmail.eugene.shchemelyov.market.service.model.AddUpdateUserDTO;
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
    private final AddUpdateUserConverter addUpdateUserConverter;

    @Autowired
    public AddUpdateUserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            GeneratorService generatorService,
            AddUpdateUserConverter addUpdateUserConverter
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.generatorService = generatorService;
        this.addUpdateUserConverter = addUpdateUserConverter;
    }

    @Override
    @Transactional
    public void update(AddUpdateUserDTO addUpdateUserDTO) {
        User user = userRepository.getById(addUpdateUserDTO.getId());
        Role role = roleRepository.getById(addUpdateUserDTO.getRole().getId());
        if (user.getRole().getName().equals(ADMINISTRATOR) &&
                !role.getName().equals(ADMINISTRATOR) &&
                (userRepository.getCountUsersWithRole(ADMINISTRATOR) <= COUNT_ADMINISTRATORS)) {
            logger.error("You can't lower privileges to the last administrator.");
            throw new ExpectedException("You can't lower privileges to the last administrator.");
        } else {
            if (addUpdateUserDTO.getName() != null) {
                user.setName(addUpdateUserDTO.getName());
            }
            if (addUpdateUserDTO.getSurname() != null) {
                user.setSurname(addUpdateUserDTO.getSurname());
            }
            if (addUpdateUserDTO.getPassword() != null) {
                user.setPassword(addUpdateUserDTO.getPassword());
            }
            if (addUpdateUserDTO.getAddress() != null) {
                user.getProfile().setAddress(addUpdateUserDTO.getAddress());
            }
            if (addUpdateUserDTO.getPhone() != null) {
                user.getProfile().setPhone(addUpdateUserDTO.getPhone());
            }
            user.setRole(role);
            userRepository.update(user);
        }
    }

    @Override
    @Transactional
    public void changePasswordById(Long id) {
        User user = userRepository.getById(id);
        user.setPassword(generatorService.getRandomPassword(PASSWORD_LENGTH));
        userRepository.update(user);
    }

    @Override
    @Transactional
    public AddUpdateUserDTO add(AddUpdateUserDTO addUpdateUserDTO) {
        User user = addUpdateUserConverter.toEntity(addUpdateUserDTO);
        if (user.getPassword() == null) {
            user.setPassword(generatorService.getRandomPassword(PASSWORD_LENGTH));
        }
        user.setDeleted(false);
        String email = addUpdateUserDTO.getEmail();
        if (userRepository.getCountUsersWithEmail(email) == 0) {
            userRepository.create(user);
        } else {
            logger.error("User exists with email: {}", email);
            throw new ExpectedException(String.format(
                    "%s: %s.", "User exists with email", email));
        }
        return addUpdateUserConverter.toDTO(user);
    }

    @Override
    @Transactional
    public AddUpdateUserDTO getById(Long id) {
        User user = userRepository.getById(id);
        return addUpdateUserConverter.toDTO(user);
    }
}
