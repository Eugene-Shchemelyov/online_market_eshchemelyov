package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.CUSTOMER_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @Before
    public void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_PROFILE" +
                    " WHERE F_USER_ID = (SELECT (F_ID) FROM T_USER WHERE F_EMAIL = 'test@test')");
            statement.executeUpdate("DELETE FROM T_USER WHERE F_EMAIL = 'test@test'");
            statement.executeUpdate("INSERT INTO T_USER (F_ID, F_PATRONYMIC, F_NAME, F_SURNAME," +
                    " F_EMAIL, F_PASSWORD, F_ROLE_ID)" +
                    " VALUES (10, 'patron', 'Name', 'test', 'test@test'," +
                    " '$2a$12$r7CgSVxKLfE8C9DzQxCnxOL9G3lFSRvyIjIa66amqkMD1zOWsXYyq', 1)");
            statement.executeUpdate("INSERT INTO T_PROFILE (F_USER_ID, F_ADDRESS, F_PHONE)\n" +
                    " VALUES (10, 'asd', 'asd')");
        }
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldShowStatus200ForUsersPage() throws Exception {
        this.mockMvc.perform(get("/private/users"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {CUSTOMER_USER})
    @Test
    public void shouldShowStatus302ForUsersPageForCustomer() throws Exception {
        this.mockMvc.perform(get("/private/users"))
                .andExpect(status().isFound());
    }

    @Test
    public void shouldShowStatus302ForUsersPage() throws Exception {
        this.mockMvc.perform(get("/private/users"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldRedirectToUsersPageAfterDeletingForAdministrator() throws Exception {
        this.mockMvc.perform(post("/private/users/delete"))
                .andExpect(redirectedUrl("/private/users"));
    }

    @Test
    public void shouldNotRedirectToUsersPageAfterDeletingForAdministrator() throws Exception {
        this.mockMvc.perform(post("/private/users/delete"))
                .andExpect(status().isFound());
    }

    @Test
    public void shouldNotRedirectToUpdateUserPageAfterChangingThePassword() throws Exception {
        this.mockMvc.perform(get("/private/users/1/password"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldShowStatus200ForAddUserPage() throws Exception {
        this.mockMvc.perform(get("/private/users/new"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForAddUserPage() throws Exception {
        this.mockMvc.perform(get("/private/users/new"))
                .andExpect(status().isFound());
    }

    @Test
    public void shouldNotRedirectWithoutAuthorityForPostAddUrl() throws Exception {
        this.mockMvc.perform(post("/private/users/new")
                .param("surname", "test")
                .param("name", "test")
                .param("patronymic", "test")
                .param("email", "test@test")
                .param("role.id", "1"))
                .andExpect(status().isFound());
    }


    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldRedirectToUsersPageAfterChangingUserRole() throws Exception {
        this.mockMvc.perform(post("/private/users/10/update/role")
                .param("role", "2"))
                .andExpect(redirectedUrl("/private/users"));
    }

    @Test
    public void shouldNotRedirectToUsersPageAfterChangingUserRole() throws Exception {
        this.mockMvc.perform(post("/private/users/10/update/role"))
                .andExpect(status().isFound());
    }
}
