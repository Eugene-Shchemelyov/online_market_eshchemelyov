package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.RoleService;
import com.gmail.eugene.shchemelyov.market.service.UserService;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private PaginationService paginationService;
    @Mock
    private RoleService roleService;

    private UserDTO userDTO = new UserDTO();
    private List<UserDTO> users = new ArrayList<>();
    private Pagination pagination = new Pagination();

    @Before
    public void initialize() {
        userDTO.setId(2L);
        userDTO.setSurname("Surname");
        userDTO.setName("Name");
        userDTO.setPatronymic("Patronymic");
        userDTO.setEmail("Email");
        userDTO.setPassword("1");
        userDTO.setRoleName(ADMINISTRATOR);
        userDTO.setDeleted(false);
        users = asList(userDTO, userDTO, userDTO);
        pagination.setCountPages(1);
        pagination.setCurrentPage(1);
        UserController userController = new UserController(userService, paginationService, roleService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldGetUsersPage() throws Exception {
        when(paginationService.getUserPagination(null)).thenReturn(pagination);
        when(userService.getUsers(pagination)).thenReturn(users);
        this.mockMvc.perform(get("/private/users"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pagination", pagination))
                .andExpect(model().attribute("users", users))
                .andExpect(forwardedUrl("user/all"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldGetUsersPageWithPageInUrl() throws Exception {
        when(paginationService.getUserPagination(1)).thenReturn(pagination);
        when(userService.getUsers(pagination)).thenReturn(users);
        this.mockMvc.perform(get("/private/users/{page}", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pagination", pagination))
                .andExpect(model().attribute("users", users))
                .andExpect(forwardedUrl("user/all"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldAddAttributeCountDeletedUsersToModel() throws Exception {
        when(paginationService.getUserPagination(null)).thenReturn(pagination);
        when(userService.getUsers(pagination)).thenReturn(users);
        this.mockMvc.perform(get("/private/users?countDeletedUsers=3"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pagination", pagination))
                .andExpect(model().attribute("users", users))
                .andExpect(model().attribute("countDeletedUsers", 3))
                .andExpect(forwardedUrl("user/all"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldAddAttributeUpdateToModel() throws Exception {
        when(paginationService.getUserPagination(null)).thenReturn(pagination);
        when(userService.getUsers(pagination)).thenReturn(users);
        this.mockMvc.perform(get("/private/users?update=true"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pagination", pagination))
                .andExpect(model().attribute("users", users))
                .andExpect(model().attribute("update", true))
                .andExpect(forwardedUrl("user/all"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldRedirectToUrlAfterDeletingWithCountDeletedUsers() throws Exception {
        String email = "Admin";
        List<String> emails = asList("Admin", "Admin");
        when(userService.deleteUsersByEmail(emails)).thenReturn(2);
        this.mockMvc.perform(post("/private/users/delete")
                .param("emails", email)
                .param("emails", email))
                .andExpect(redirectedUrl("/private/users?countDeletedUsers=2"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldGetAddUserPage() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        this.mockMvc.perform(get("/private/users/add"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/add"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldRedirectToUrlAfterAdding() throws Exception {
        this.mockMvc.perform(post("/private/users/add")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(redirectedUrl("/private/users"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToPageAddUserIfHasErrorsTheSurnameMoreThanMax() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setSurname("1111111111111111111111111111111111111111111111111111111111111111111111111111");
        this.mockMvc.perform(post("/private/users/add")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/add"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToThePageUserAddIfHasErrorsTheSurnameIsNull() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setSurname(null);
        this.mockMvc.perform(post("/private/users/add")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/add"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToThePageUserAddIfHasErrorsTheNameMoreThanMax() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setName("1111111111111111111111111111111111111111111111111111111111111111111111111111");
        this.mockMvc.perform(post("/private/users/add")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/add"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToThePageUserAddIfHasErrorsTheNameIsNull() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setName(null);
        this.mockMvc.perform(post("/private/users/add")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/add"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToThePageUserAddIfHasErrorsThePatronymicMoreThanMax() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setPatronymic("1111111111111111111111111111111111111111111111111111111111111111111111111111");
        this.mockMvc.perform(post("/private/users/add")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/add"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToThePageUserAddIfHasErrorsThePatronymicIsNull() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setPatronymic(null);
        this.mockMvc.perform(post("/private/users/add")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/add"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToThePageUserAddIfHasErrorsTheEmailMoreThanMax() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setEmail("1111111111111111111111111111111111111111111111111111111111111111111111111111");
        this.mockMvc.perform(post("/private/users/add")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/add"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToThePageUserAddIfHasErrorsTheEmailIsNull() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setEmail(null);
        this.mockMvc.perform(post("/private/users/add")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/add"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToThePageUserAddIfHasErrorsTheRoleIsNull() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setRoleName(null);
        this.mockMvc.perform(post("/private/users/add")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/add"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldGetTheUpdateUserPage() throws Exception {
        when(userService.loadUserById(1L)).thenReturn(userDTO);
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        this.mockMvc.perform(get("/private/users/{id}/update", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", userDTO))
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/update"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldGetTheUpdateUserPageAndAddAttributeToModel() throws Exception {
        when(userService.loadUserById(1L)).thenReturn(userDTO);
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        this.mockMvc.perform(get("/private/users/{id}/update?message=true", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", userDTO))
                .andExpect(model().attribute("roles", roles))
                .andExpect(model().attribute("message", true))
                .andExpect(forwardedUrl("user/update"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldRedirectToUsersPageAfterUpdateUser() throws Exception {
        this.mockMvc.perform(post("/private/users/{id}/update", "1")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(redirectedUrl("/private/users?update=true"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToTheUserUpdatePageIfHasErrorsTheSurnameMoreThanMax() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setSurname("1111111111111111111111111111111111111111111111111111111111111111111111111111");
        this.mockMvc.perform(post("/private/users/{id}/update", "1")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/update"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToTheUserUpdatePageIfHasErrorsTheSurnameIsNull() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setSurname(null);
        this.mockMvc.perform(post("/private/users/{id}/update", "1")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/update"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToTheUserUpdatePageIfHasErrorsTheNameMoreThanMax() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setName("1111111111111111111111111111111111111111111111111111111111111111111111111111");
        this.mockMvc.perform(post("/private/users/{id}/update", "1")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/update"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToTheUserUpdatePageIfHasErrorsTheNameIsNull() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setName(null);
        this.mockMvc.perform(post("/private/users/{id}/update", "1")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/update"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToTheUserUpdatePageIfHasErrorsThePatronymicMoreThanMax() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setPatronymic("1111111111111111111111111111111111111111111111111111111111111111111111111111");
        this.mockMvc.perform(post("/private/users/{id}/update", "1")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/update"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToTheUserUpdatePageIfHasErrorsThePatronymicIsNull() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setPatronymic(null);
        this.mockMvc.perform(post("/private/users/{id}/update", "1")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/update"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToTheUserUpdatePageIfHasErrorsTheEmailMoreThanMax() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setEmail("1111111111111111111111111111111111111111111111111111111111111111111111111111");
        this.mockMvc.perform(post("/private/users/{id}/update", "1")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/update"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToTheUserUpdatePageIfHasErrorsTheEmailIsNull() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setEmail(null);
        this.mockMvc.perform(post("/private/users/{id}/update", "1")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/update"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldReturnToTheUserUpdatePageIfHasErrorsTheRoleIsNull() throws Exception {
        List<String> roles = asList("Admin", "Admin2");
        when(roleService.getAllRoles()).thenReturn(roles);
        userDTO.setRoleName(null);
        this.mockMvc.perform(post("/private/users/{id}/update", "1")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", roles))
                .andExpect(forwardedUrl("user/update"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldRedirectToTheUsersPageAfterChangingUserPassword() throws Exception {
        doNothing().when(userService).changePassword(1L);
        this.mockMvc.perform(post("/private/users/{id}/message", "1"))
                .andExpect(redirectedUrl("/private/users/1/update?message=true"));
    }
}
