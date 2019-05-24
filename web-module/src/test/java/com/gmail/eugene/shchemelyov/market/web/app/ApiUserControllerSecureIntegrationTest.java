package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SECURE_REST_API;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiUserControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @Before
    public void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_PROFILE" +
                    " WHERE F_USER_ID = (SELECT F_ID FROM T_USER WHERE F_EMAIL = 'apiAddUser@mail.ru')");
            statement.executeUpdate("DELETE FROM T_USER WHERE F_EMAIL = 'apiAddUser@mail.ru'");
        }
    }

    @WithMockUser(authorities = {SECURE_REST_API})
    @Test
    public void shouldGetSucceedWith200ForAddUserApi() throws Exception {
        this.mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{" +
                        "\"surname\": \"apipip\"," +
                        "\"name\": \"pop\"," +
                        "\"email\": \"apiAddUser@mail.ru\"," +
                        "\"role\": {" +
                        "\"id\": \"1\"" +
                        "}," +
                        "\"phone\": \"+12345678990\"," +
                        "\"address\": \"Pushkin street\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldGetStatus302ForAdministrator() throws Exception {
        this.mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{" +
                        "\"surname\" : \"apipip\"," +
                        "\"name\" : \"pop\"," +
                        "\"email\" : \"apiAddUser@mail.ru\"," +
                        "\"role\" : {" +
                        "\"id\" : \"1\"" +
                        "}," +
                        "\"phone\" : \"+12345678990\"," +
                        "\"address\" : \"Pushkin street\"" +
                        "}"))
                .andExpect(status().isFound());
    }
}
