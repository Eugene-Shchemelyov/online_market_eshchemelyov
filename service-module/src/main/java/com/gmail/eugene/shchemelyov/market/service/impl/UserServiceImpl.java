package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.RoleRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.GeneratorService;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.UserService;
import com.gmail.eugene.shchemelyov.market.service.converter.UserConverter;
import com.gmail.eugene.shchemelyov.market.service.exception.ServiceException;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.SERVICE_ERROR_MESSAGE;
import static com.gmail.eugene.shchemelyov.market.service.constant.ExceptionMessageConstant.TRANSACTION_ERROR_MESSAGE;
import static com.gmail.eugene.shchemelyov.market.service.constant.GeneratorServiceConstant.PASSWORD_LENGTH;
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.COUNT_ADMINISTRATORS;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserConverter userConverter;
    private final GeneratorService generatorService;
    private final PaginationService paginationService;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserConverter userConverter,
            GeneratorService generatorService,
            PaginationService paginationService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userConverter = userConverter;
        this.generatorService = generatorService;
        this.paginationService = paginationService;
    }

    @Override
    public UserDTO loadUserByEmail(String email) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = userRepository.loadUserByEmail(connection, email);
                UserDTO userDTO = userConverter.toDTO(user);
                connection.commit();
                return userDTO;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s. %s: %s.", TRANSACTION_ERROR_MESSAGE, "When found the user with email", email), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s. %s: %s.", SERVICE_ERROR_MESSAGE, "When found the user with email", email), e);
        }
    }

    @Override
    @Transactional
    public UserDTO getById(Long id) {
        User user = userRepository.getById(id);
        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public Pagination getLimitUsers(Integer page) {
        Pagination pagination = paginationService.getUserPagination(page);
        List<User> users = userRepository.getLimitUsers(pagination);
        List<UserDTO> userDTOS = users.stream()
                .map(userConverter::toDTO)
                .collect(Collectors.toList());
        pagination.setEntities(userDTOS);
        return pagination;
    }

    @Override
    @Transactional
    public void deleteUsersById(List<Long> usersIds) {
        Integer countDeletedAdministrators = 0;
        for (Long userId : usersIds) {
            User user = userRepository.getById(userId);
            if (user.getRole().getName().equals(ADMINISTRATOR)) {
                countDeletedAdministrators++;
            }
            if (user.getRole().getName().equals(ADMINISTRATOR) &&
                    getCountUserWithRole(ADMINISTRATOR).equals(countDeletedAdministrators)) {
                logger.error("You can't delete the last administrator.");
                throw new ExpectedException("You can't delete the last administrator.");
            } else {
                userRepository.delete(user);
            }
        }
    }

    @Override
    @Transactional
    public void update(UserDTO userDTO) {
        User user = userRepository.getById(userDTO.getId());
        Role role = roleRepository.getById(userDTO.getRole().getId());
        if (user.getRole().getName().equals(ADMINISTRATOR) &&
                !role.getName().equals(ADMINISTRATOR) &&
                (getCountUserWithRole(ADMINISTRATOR) <= COUNT_ADMINISTRATORS)) {
            logger.error("You can't lower privileges to the last administrator.");
            throw new ExpectedException("You can't lower privileges to the last administrator.");
        } else {
            if (userDTO.getName() != null) {
                user.setName(userDTO.getName());
            }
            if (userDTO.getSurname() != null) {
                user.setSurname(userDTO.getSurname());
            }
            if (userDTO.getPassword() != null) {
                user.setPassword(userDTO.getPassword());
            }
            if (userDTO.getProfile().getAddress() != null) {
                user.getProfile().setAddress(userDTO.getProfile().getAddress());
            }
            if (userDTO.getProfile().getPhone() != null) {
                user.getProfile().setPhone(userDTO.getProfile().getPhone());
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
    public void add(UserDTO userDTO) {
        userDTO.setDeleted(false);
        User user = userConverter.toEntity(userDTO);
        if (user.getPassword() == null) {
            user.setPassword(generatorService.getRandomPassword(PASSWORD_LENGTH));
        }
        userRepository.create(user);
    }

    private Integer getCountUserWithRole(String roleName) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer countUsersWithRole = userRepository.getCountUsersWithRole(connection, roleName, false);
                connection.commit();
                return countUsersWithRole;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format("%s. %s: %s.",
                        TRANSACTION_ERROR_MESSAGE, "When getting count users with role", roleName), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format("%s. %s %s.",
                    SERVICE_ERROR_MESSAGE, "When getting count users with role", roleName), e);
        }
    }
}
