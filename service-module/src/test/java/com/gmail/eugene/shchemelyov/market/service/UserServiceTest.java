package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.RoleRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.UserConverter;
import com.gmail.eugene.shchemelyov.market.service.impl.UserServiceImpl;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.repository.constant.UserConstant.DELETED;
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private GeneratorService generatorService;
    @Mock
    private Connection connection;

    private UserDTO userDTO = new UserDTO();
    private User user = new User();
    private Role role = new Role();
    private List<User> users = new ArrayList<>();
    private List<UserDTO> userDTOs = new ArrayList<>();
    private Pagination pagination = new Pagination();

    @Before
    public void initialize() {
        role.setId(1L);
        role.setName(ADMINISTRATOR);

        user.setId(2L);
        user.setSurname("Surname");
        user.setName("Name");
        user.setPatronymic("Patronymic");
        user.setEmail("Email");
        user.setPassword("1");
        Role roleFromDatabase = new Role();
        roleFromDatabase.setId(1L);
        user.setRole(roleFromDatabase);
        user.setDeleted(false);

        userDTO.setId(2L);
        userDTO.setSurname("Surname");
        userDTO.setName("Name");
        userDTO.setPatronymic("Patronymic");
        userDTO.setEmail("Email");
        userDTO.setPassword("1");
        userDTO.setRoleName(ADMINISTRATOR);
        userDTO.setDeleted(false);

        pagination.setCountPages(1);
        pagination.setCurrentPage(1);

        users = asList(user, user, user);
        userDTOs = asList(userDTO, userDTO, userDTO);

        userService = new UserServiceImpl(userRepository, roleRepository, userConverter, generatorService);
        when(userRepository.getConnection()).thenReturn(connection);
    }

    @Test
    public void shouldLoadUserByEmailAndReturnUserDTO() {
        when(userRepository.loadUserByEmail(connection, "Email")).thenReturn(user);
        when(roleRepository.getRoleById(connection, user.getRole().getId())).thenReturn(role);
        user.setRole(role);
        when(userConverter.toUserDTO(user)).thenReturn(userDTO);
        UserDTO loadedUser = userService.loadUserByEmail("Email");
        Assert.assertEquals(userDTO, loadedUser);
    }

    @Test
    public void shouldLoadUserByIdAndReturnUserDTO() {
        when(userRepository.loadUserById(connection, 2L)).thenReturn(user);
        when(roleRepository.getRoleById(connection, user.getRole().getId())).thenReturn(role);
        user.setRole(role);
        when(userConverter.toUserDTO(user)).thenReturn(userDTO);
        UserDTO loadedUser = userService.loadUserById(2L);
        Assert.assertEquals(userDTO, loadedUser);
    }

    @Test
    public void shouldLoadUsers() {
        when(userRepository.getLimitUsers(connection, pagination)).thenReturn(users);
        when(roleRepository.getRoleById(connection, user.getRole().getId())).thenReturn(role);
        user.setRole(role);
        when(userConverter.toUserDTO(user)).thenReturn(userDTO);

        List<UserDTO> loadedUserDTOs = userService.getUsers(pagination);
        Assert.assertEquals(userDTOs, loadedUserDTOs);
    }

    @Test
    public void shouldDeleteUserIfHeIsNotAnAdministrator() {
        when(roleRepository.getRoleNameByUserEmail(connection, "Email")).thenReturn("testFalse");
        when(userRepository.deleteByEmail(connection, "Email", !DELETED)).thenReturn(1);

        Integer count = userService.deleteUsersByEmail(asList("Email", "Email", "Email"));
        Integer expected = 3;
        Assert.assertEquals(expected, count);
    }

    @Test
    public void shouldDeleteUserIfHeIsNotTheLastAdministrator() {
        when(roleRepository.getRoleNameByUserEmail(connection, "Email")).thenReturn(ADMINISTRATOR);
        when(userRepository.getCountUsersWithRole(connection, ADMINISTRATOR, DELETED)).thenReturn(2);
        when(userRepository.deleteByEmail(connection, "Email", !DELETED)).thenReturn(1);

        Integer count = userService.deleteUsersByEmail(asList("Email", "Email", "Email"));
        Integer expected = 3;
        Assert.assertEquals(expected, count);
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotDeleteUserWhoIsTheLastAdministrator() {
        when(roleRepository.getRoleNameByUserEmail(connection, "Email")).thenReturn(ADMINISTRATOR);
        when(userRepository.getCountUsersWithRole(connection, ADMINISTRATOR, DELETED)).thenReturn(1);

        userService.deleteUsersByEmail(asList("Email", "Email", "Email"));
    }

    @Test
    public void shouldUpdateUserIfHeIsNotAnAdministratorAndLowerPrivileges() {
        role.setName("testFalse");
        when(roleRepository.getRoleByName(connection, userDTO.getRoleName())).thenReturn(role);
        user.setRole(role);
        when(userConverter.toUser(userDTO, role)).thenReturn(user);
        when(roleRepository.getRoleNameByUserId(connection, user.getId())).thenReturn(ADMINISTRATOR);
        doNothing().when(userRepository).update(connection, user);

        userService.update(userDTO);
    }

    @Test
    public void shouldUpdateUserIfHeIsNotTheLastAdministratorAndLowerPrivileges() {
        userDTO.setRoleName("testFalse");
        when(roleRepository.getRoleByName(connection, userDTO.getRoleName())).thenReturn(role);
        user.setRole(role);
        when(userConverter.toUser(userDTO, role)).thenReturn(user);
        when(roleRepository.getRoleNameByUserId(connection, user.getId())).thenReturn(ADMINISTRATOR);
        when(userRepository.getCountUsersWithRole(connection, ADMINISTRATOR, DELETED)).thenReturn(2);
        doNothing().when(userRepository).update(connection, user);

        userService.update(userDTO);
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotLowerPrivilegesToTheLastAdministrator() {
        userDTO.setRoleName("testFalse");
        when(roleRepository.getRoleByName(connection, userDTO.getRoleName())).thenReturn(role);
        user.setRole(role);
        when(userConverter.toUser(userDTO, role)).thenReturn(user);
        when(roleRepository.getRoleNameByUserId(connection, user.getId())).thenReturn(ADMINISTRATOR);
        when(userRepository.getCountUsersWithRole(connection, ADMINISTRATOR, DELETED)).thenReturn(1);

        userService.update(userDTO);
    }
}
