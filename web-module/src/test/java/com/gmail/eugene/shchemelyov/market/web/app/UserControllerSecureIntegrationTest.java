package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Before;
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
    @Before
    public void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_PROFILE" +
                    " WHERE F_USER_ID = (SELECT (F_ID) FROM T_USER WHERE F_EMAIL = 'test@test')");
            statement.executeUpdate("DELETE FROM T_USER WHERE F_EMAIL = 'test@test'");
            statement.executeUpdate("INSERT INTO T_USER (F_ID, F_NAME, F_SURNAME," +
                    " F_EMAIL, F_PASSWORD, F_ROLE_ID)" +
                    " VALUES (10, 'Name', 'test', 'test@test'," +
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

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldRedirectWithAuthorityForPostAddUrl() throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_PROFILE" +
                    " WHERE F_USER_ID = (SELECT (F_ID) FROM T_USER WHERE F_EMAIL = 'test@test')");
            statement.executeUpdate("DELETE FROM T_USER WHERE F_EMAIL = 'test@test'");
        }
        this.mockMvc.perform(post("/private/users/new")
                .param("surname", "test")
                .param("name", "test")
                .param("email", "test@test")
                .param("phone", "+345678012323")
                .param("role.id", "1")
                .param("address", "test"))
                .andExpect(redirectedUrl("/private/users"));
    }

    @Test
    public void shouldNotRedirectWithoutAuthorityForPostAddUrl() throws Exception {
        this.mockMvc.perform(post("/private/users/new")
                .param("surname", "test")
                .param("name", "test")
                .param("email", "test@test")
                .param("phone", "+345678012323")
                .param("role.id", "1")
                .param("address", "test"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldShowStatus200ForUpdateUserPage() throws Exception {
        this.mockMvc.perform(get("/private/users/1/update"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForUpdateUserPage() throws Exception {
        this.mockMvc.perform(get("/private/users/1/update"))
                .andExpect(status().isFound());
    }


    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldRedirectToUsersPageAfterUpdatingUser() throws Exception {
        this.mockMvc.perform(post("/private/users/10/update")
                .param("surname", "test")
                .param("name", "test")
                .param("email", "test@test")
                .param("phone", "+345678012323")
                .param("role.id", "1")
                .param("address", "test"))
                .andExpect(redirectedUrl("/private/users?update=true"));
    }

    @Test
    public void shouldNotRedirectToUsersPageAfterUpdatingUser() throws Exception {
        this.mockMvc.perform(post("/private/users/update"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldRedirectToUpdateUserPageAfterChangingThePassword() throws Exception {
        this.mockMvc.perform(post("/private/users/1/password"))
                .andExpect(redirectedUrl("/private/users/1/update?message=true"));
    }

    @Test
    public void shouldNotRedirectToUpdateUserPageAfterChangingThePassword() throws Exception {
        this.mockMvc.perform(post("/private/users/1/password"))
                .andExpect(status().isFound());
    }
}
