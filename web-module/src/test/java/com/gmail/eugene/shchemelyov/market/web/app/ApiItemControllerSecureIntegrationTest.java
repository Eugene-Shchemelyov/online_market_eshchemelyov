package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SECURE_REST_API;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiItemControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @Before
    public void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_ITEM WHERE F_ID = 1");
            statement.executeUpdate("INSERT INTO T_ITEM (F_ID, F_NAME, F_UNIQUE_NUMBER, F_PRICE, F_DESCRIPTION)" +
                    " VALUES (1, 'test', '975f837b-0436-49d2-8107-b4d8b4287b82', '100', 'test')");
        }
    }

    @WithMockUser(authorities = {SECURE_REST_API})
    @Test
    public void shouldGetSucceedWith200ForGetItemsApi() throws Exception {
        this.mockMvc.perform(get("/api/v1/items"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {SECURE_REST_API})
    @Test
    public void shouldGetSucceedWith200ForAddItemApi() throws Exception {
        this.mockMvc.perform(post("/api/v1/items")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{" +
                        "\"name\": \"itemAPI\"," +
                        "\"price\": \"250\"," +
                        "\"description\": \"Descrtiption item\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {SECURE_REST_API})
    @Test
    public void shouldGetSucceedWith200ForShowItemApi() throws Exception {
        this.mockMvc.perform(get("/api/v1/items/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {SECURE_REST_API})
    @Test
    public void shouldGetSucceedWith200ForDeleteItemApi() throws Exception {
        this.mockMvc.perform(delete("/api/v1/items/1"))
                .andExpect(status().isOk());
    }
}
