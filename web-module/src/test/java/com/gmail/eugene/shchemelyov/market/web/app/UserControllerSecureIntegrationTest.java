package com.gmail.eugene.shchemelyov.market.web.app;

import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerSecureIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    private UserDTO userDTO = new UserDTO();

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
        userDTO.setPatronymic("Patronymic");
        userDTO.setEmail("Email@mail.ru");
        userDTO.setPassword("1");
        userDTO.setRoleName(ADMINISTRATOR);
        userDTO.setDeleted(false);

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
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
                .andExpect(redirectedUrl("/private/administrator/users?countDeletedUsers=0"));
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
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
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
                .param("name", userDTO.getName())
                .param("patronymic", userDTO.getPatronymic())
                .param("email", userDTO.getEmail())
                .param("roleName", userDTO.getRoleName()))
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
