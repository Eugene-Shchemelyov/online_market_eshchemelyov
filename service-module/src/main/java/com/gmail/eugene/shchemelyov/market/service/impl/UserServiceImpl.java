package com.gmail.eugene.shchemelyov.market.service.impl;

import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.ExpectedException;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.UserService;
import com.gmail.eugene.shchemelyov.market.service.converter.UserConverter;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.COUNT_ADMINISTRATORS;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PaginationService paginationService;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            UserConverter userConverter,
            PaginationService paginationService
    ) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.paginationService = paginationService;
    }

    @Override
    @Transactional
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.loadUserByEmail(email, false);
        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO getById(Long id) {
        User user = userRepository.getById(id);
        return userConverter.toDTO(user);
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public Pagination getLimitUsers(Integer page) {
        Pagination pagination = paginationService.getUserPagination(page);
        List<User> users = userRepository.getLimitUsers(pagination, false);
        List<UserDTO> userDTOS = users.stream()
                .map(userConverter::toDTO)
                .collect(Collectors.toList());
        pagination.setEntities(userDTOS);
        return pagination;
    }

    @Override
    @Transactional
    public void deleteUsersById(List<Long> usersIds) {
        for (Long userId : usersIds) {
            User user = userRepository.getById(userId);
            if (user.getRole().getName().equals(ADMINISTRATOR) &&
                    (userRepository.getCountUsersWithRole(ADMINISTRATOR, false) <= COUNT_ADMINISTRATORS)) {
                logger.error("You can't delete the last administrator.");
                throw new ExpectedException("You can't delete the last administrator.");
            } else {
                userRepository.delete(user);
            }
        }
    }
}

