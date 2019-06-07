package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SECURE_REST_API;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldGetStatus302ForAdministrator() throws Exception {
        this.mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{" +
                        "\"surname\": \"apipip\"," +
                        "\"name\": \"pop\"," +
                        "\"patronymic\": \"patronymic\"," +
                        "\"email\": \"apiAddUser@mail.ru\"," +
                        "\"role\": {" +
                        "\"id\": 1" +
                        "}" +
                        "}"))
                .andExpect(status().isFound());
    }
}
