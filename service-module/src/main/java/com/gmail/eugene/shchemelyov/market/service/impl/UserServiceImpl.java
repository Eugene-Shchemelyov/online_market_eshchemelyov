package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.RoleRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.GeneratorService;
import com.gmail.eugene.shchemelyov.market.service.UserService;
import com.gmail.eugene.shchemelyov.market.service.converter.UserConverter;
import com.gmail.eugene.shchemelyov.market.service.exception.ServiceException;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserConverter userConverter,
            GeneratorService generatorService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userConverter = userConverter;
        this.generatorService = generatorService;
    }

    @Override
    public UserDTO loadUserByEmail(String email) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = userRepository.loadUserByEmail(connection, email);
                UserDTO userDTO = getUserDTO(connection, user);
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
    public UserDTO loadUserById(Long id) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = userRepository.loadUserById(connection, id);
                UserDTO userDTO = getUserDTO(connection, user);
                connection.commit();
                return userDTO;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s. %s: %d.", TRANSACTION_ERROR_MESSAGE, "When found the user with id", id), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s. %s: %d.", SERVICE_ERROR_MESSAGE, "When found the user with id", id), e);
        }
    }

    @Override
    public List<UserDTO> getUsers(Pagination pagination) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<UserDTO> userDTOs = getUserDTOs(connection, pagination);
                connection.commit();
                return userDTOs;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s. %s.", TRANSACTION_ERROR_MESSAGE, "When found limit users"), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s. %s.", SERVICE_ERROR_MESSAGE, "When found limit users"), e);
        }
    }

    @Override
    public Integer deleteUsersByEmail(List<String> emails) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer countDelete = deleteUsers(connection, emails);
                connection.commit();
                return countDelete;
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s. %s: %s.", TRANSACTION_ERROR_MESSAGE, "When deleting users", emails.toString()), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s. %s: %s.", SERVICE_ERROR_MESSAGE, "When deleting users", emails.toString()), e);
        }
    }

    @Override
    public void update(UserDTO userDTO) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                updateUser(connection, userDTO);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format("%s. %s: %d.",
                        TRANSACTION_ERROR_MESSAGE, "When updating the user with id", userDTO.getId()), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format("%s. %s: %d.",
                    SERVICE_ERROR_MESSAGE, "When updating the user with id", userDTO.getId()), e);
        }
    }

    @Override
    public void changePassword(Long id) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = new User();
                user.setId(id);
                user.setPassword(generatorService.getRandomPassword(PASSWORD_LENGTH));
                userRepository.updatePassword(connection, user);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s. %s: %d.", TRANSACTION_ERROR_MESSAGE, "When changing the user password with id", id), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s. %s: %d.", SERVICE_ERROR_MESSAGE, "When changing the user password with id", id), e);
        }
    }

    @Override
    public void add(UserDTO userDTO) {
        try (Connection connection = userRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = getUser(connection, userDTO);
                user.setDeleted(false);
                user.setPassword(generatorService.getRandomPassword(PASSWORD_LENGTH));
                userRepository.add(connection, user);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ServiceException(String.format(
                        "%s. %s.", TRANSACTION_ERROR_MESSAGE, "When adding the user"), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(String.format(
                    "%s. %s.", SERVICE_ERROR_MESSAGE, "When adding the user"), e);
        }
    }

    private List<UserDTO> getUserDTOs(Connection connection, Pagination pagination) {
        Integer startLimitPosition = (pagination.getCurrentPage() - 1) * pagination.getLimitOnPage();
        pagination.setStartLimitPosition(startLimitPosition);
        List<User> users = userRepository.getLimitUsers(connection, pagination);
        return users.stream()
                .map(user -> getUserDTO(connection, user))
                .collect(Collectors.toList());
    }

    private UserDTO getUserDTO(Connection connection, User user) {
        Long roleId = user.getRole().getId();
        Role role = roleRepository.getRoleById(connection, roleId);
        user.setRole(role);
        return userConverter.toUserDTO(user);
    }

    private User getUser(Connection connection, UserDTO userDTO) {
        Role role = roleRepository.getRoleByName(connection, userDTO.getRoleName());
        return userConverter.toUser(userDTO, role);
    }

    private Integer deleteUsers(Connection connection, List<String> emails) {
        Integer countDeletedUsers = 0;
        for (String email : emails) {
            if (roleRepository.getRoleNameByUserEmail(connection, email).equals(ADMINISTRATOR)) {
                if (userRepository.getCountUsersWithRole(connection, ADMINISTRATOR, false) > COUNT_ADMINISTRATORS) {
                    countDeletedUsers += userRepository.deleteByEmail(connection, email, true);
                } else {
                    logger.error("You can't delete the last administrator.");
                    throw new ExpectedException("You can't delete the last administrator.");
                }
            } else {
                countDeletedUsers += userRepository.deleteByEmail(connection, email, true);
            }
        }
        return countDeletedUsers;
    }

    private void updateUser(Connection connection, UserDTO userDTO) {
        User user = getUser(connection, userDTO);
        String roleName = roleRepository.getRoleNameByUserId(connection, user.getId());
        if (roleName.equals(ADMINISTRATOR) && !userDTO.getRoleName().equals(ADMINISTRATOR)) {
            if (userRepository.getCountUsersWithRole(connection, ADMINISTRATOR, false) > COUNT_ADMINISTRATORS) {
                userRepository.update(connection, user);
            } else {
                logger.error("You can't lower privileges to the last administrator.");
                throw new ExpectedException("You can't lower privileges to the last administrator.");
            }
        } else {
            userRepository.update(connection, user);
        }
    }
}
