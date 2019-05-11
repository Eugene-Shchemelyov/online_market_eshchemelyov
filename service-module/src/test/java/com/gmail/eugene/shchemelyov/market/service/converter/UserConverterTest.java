package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.impl.UserConverterImpl;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;

@RunWith(MockitoJUnitRunner.class)
public class UserConverterTest {
    private UserConverter userConverter;
    private User user = new User();
    private UserDTO userDTO = new UserDTO();
    private Role role = new Role();

    @Before
    public void initialize() {
        userDTO.setId(1L);
        userDTO.setSurname("Surname");
        userDTO.setName("Name");
        userDTO.setPatronymic("Patronymic");
        userDTO.setEmail("Email");
        userDTO.setPassword("1");
        userDTO.setRoleName(ADMINISTRATOR);
        userDTO.setDeleted(false);

        user.setId(1L);
        user.setSurname("Surname");
        user.setName("Name");
        user.setPatronymic("Patronymic");
        user.setEmail("Email");
        user.setPassword("1");
        Role roleFromDatabase = new Role();
        roleFromDatabase.setId(1L);
        user.setRole(roleFromDatabase);
        user.setDeleted(false);

        userConverter = new UserConverterImpl();
    }

    @Test
    public void shouldConvertedUserIdToUserDTOId() {
        UserDTO loadedUserDTO = userConverter.toUserDTO(user);
        Assert.assertEquals(userDTO.getId(), loadedUserDTO.getId());
    }

    @Test
    public void shouldConvertedUserSurnameToUserDTOSurname() {
        UserDTO loadedUserDTO = userConverter.toUserDTO(user);
        Assert.assertEquals(userDTO.getSurname(), loadedUserDTO.getSurname());
    }

    @Test
    public void shouldConvertedUserNameToUserDTOUserName() {
        UserDTO loadedUserDTO = userConverter.toUserDTO(user);
        Assert.assertEquals(userDTO.getName(), loadedUserDTO.getName());
    }

    @Test
    public void shouldConvertedUserPatronymicToUserDTOPatronymic() {
        UserDTO loadedUserDTO = userConverter.toUserDTO(user);
        Assert.assertEquals(userDTO.getPatronymic(), loadedUserDTO.getPatronymic());
    }

    @Test
    public void shouldConvertedUserEmailToUserDTOEmail() {
        UserDTO loadedUserDTO = userConverter.toUserDTO(user);
        Assert.assertEquals(userDTO.getEmail(), loadedUserDTO.getEmail());
    }

    @Test
    public void shouldConvertedUserRoleNameToUserDTORoleName() {
        role.setName(ADMINISTRATOR);
        user.setRole(role);
        UserDTO loadedUserDTO = userConverter.toUserDTO(user);
        Assert.assertEquals(userDTO.getRoleName(), loadedUserDTO.getRoleName());
    }

    @Test
    public void shouldConvertedUserDeletedToUserDTODeleted() {
        UserDTO loadedUserDTO = userConverter.toUserDTO(user);
        Assert.assertEquals(userDTO.isDeleted(), loadedUserDTO.isDeleted());
    }

    @Test
    public void shouldConvertedUserDTOIdToUserId() {
        role.setId(1L);
        User loadedUser = userConverter.toUser(userDTO, role);
        Assert.assertEquals(user.getId(), loadedUser.getId());
    }

    @Test
    public void shouldConvertedUserDTOSurnameToUserSurname() {
        role.setId(1L);
        User loadedUser = userConverter.toUser(userDTO, role);
        Assert.assertEquals(user.getSurname(), loadedUser.getSurname());
    }

    @Test
    public void shouldConvertedUserDTOUserNameToUserName() {
        role.setId(1L);
        User loadedUser = userConverter.toUser(userDTO, role);
        Assert.assertEquals(user.getName(), loadedUser.getName());
    }

    @Test
    public void shouldConvertedUserDTOPatronymicToUserPatronymic() {
        role.setId(1L);
        User loadedUser = userConverter.toUser(userDTO, role);
        Assert.assertEquals(user.getPatronymic(), loadedUser.getPatronymic());
    }

    @Test
    public void shouldConvertedUserDTOEmailToUserEmail() {
        role.setId(1L);
        User loadedUser = userConverter.toUser(userDTO, role);
        Assert.assertEquals(user.getEmail(), loadedUser.getEmail());
    }

    @Test
    public void shouldConvertedUserDTORoleNameToUserRoleName() {
        role.setId(1L);
        role.setName(ADMINISTRATOR);
        user.setRole(role);
        User loadedUser = userConverter.toUser(userDTO, role);
        Assert.assertEquals(user.getRole().getName(), loadedUser.getRole().getName());
    }

    @Test
    public void shouldConvertedUserDTODeletedToUserDeleted() {
        role.setId(1L);
        User loadedUser = userConverter.toUser(userDTO, role);
        Assert.assertEquals(user.isDeleted(), loadedUser.isDeleted());
    }
}
