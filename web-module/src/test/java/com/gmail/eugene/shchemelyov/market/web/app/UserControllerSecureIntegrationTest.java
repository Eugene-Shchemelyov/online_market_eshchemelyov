package com.gmail.eugene.shchemelyov.market.web.app;

import com.gmail.eugene.shchemelyov.market.service.model.ProfileDTO;
import com.gmail.eugene.shchemelyov.market.service.model.RoleDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    private UserDTO userDTO = new UserDTO();
    private ProfileDTO profileDTO = new ProfileDTO();
    private RoleDTO roleDTO = new RoleDTO();

    @BeforeClass
    public static void deleteUserFromDatabase() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_USER WHERE F_EMAIL = 'Email@mail.ru'");
        }
    }

    @Before
    public void initialize() {
        userDTO.setId(100L);
        userDTO.setSurname("Surname");
        userDTO.setName("Name");
        userDTO.setEmail("Email@mail.ru");
        userDTO.setPassword("1");
        userDTO.setDeleted(false);
        profileDTO.setPhone("+334343434");
        profileDTO.setAddress("Adress adresss aressds");
        roleDTO.setId(1L);
        roleDTO.setName(ADMINISTRATOR);
        userDTO.setProfile(profileDTO);
        userDTO.setRole(roleDTO);
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldShowStatus200ForUsersPage() throws Exception {
        this.mockMvc.perform(get("/private/administrator/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForUsersPage() throws Exception {
        this.mockMvc.perform(get("/private/administrator/users"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldShowStatus200ForAddUserPage() throws Exception {
        this.mockMvc.perform(get("/private/administrator/users/add"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForAddUserPage() throws Exception {
        this.mockMvc.perform(get("/private/administrator/users/add"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldShowStatus200ForUpdateUserPage() throws Exception {
        this.mockMvc.perform(get("/private/administrator/users/update?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForUpdateUserPage() throws Exception {
        this.mockMvc.perform(get("/private/administrator/users/update?id=1"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldRedirectWithAuthorityForPostDeleteUrl() throws Exception {
        this.mockMvc.perform(post("/private/administrator/users/delete"))
                .andExpect(redirectedUrl("/private/administrator/users"));
    }

    @Test
    public void shouldNotRedirectWithoutAuthorityForPostDeleteUrl() throws Exception {
        this.mockMvc.perform(post("/private/administrator/users/delete"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldRedirectWithAuthorityForPostAddUrl() throws Exception {
        this.mockMvc.perform(post("/private/administrator/users/add")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName())
                .param("email", userDTO.getEmail())
                .param("profile", userDTO.getProfile().toString())
                .param("role", userDTO.getRole().toString()))
                .andExpect(redirectedUrl("/private/administrator/users"));
    }

    @Test
    public void shouldNotRedirectWithoutAuthorityForPostAddUrl() throws Exception {
        this.mockMvc.perform(post("/private/administrator/users/add"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldRedirectWithAuthorityForPostUpdateUrl() throws Exception {
        this.mockMvc.perform(post("/private/administrator/users/update")
                .param("id", "1")
                .param("surname", userDTO.getSurname())
                .param("name", userDTO.getName()))
                .andExpect(redirectedUrl("/private/administrator/users?update=true"));
    }

    @Test
    public void shouldNotRedirectWithoutAuthorityForPostUpdateUrl() throws Exception {
        this.mockMvc.perform(post("/private/administrator/users/update"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldRedirectWithAuthorityForPostMessageUrl() throws Exception {
        this.mockMvc.perform(post("/private/administrator/users/message")
                .param("id", "1"))
                .andExpect(redirectedUrl("/private/administrator/users/update?id=1&message=true"));
    }

    @Test
    public void shouldNotRedirectWithoutAuthorityForPostMessageUrl() throws Exception {
        this.mockMvc.perform(post("/private/administrator/users/message"))
                .andExpect(status().isFound());
    }
}
