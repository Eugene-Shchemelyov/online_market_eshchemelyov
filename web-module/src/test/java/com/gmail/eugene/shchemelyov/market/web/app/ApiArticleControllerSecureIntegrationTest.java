package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SECURE_REST_API;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiArticleControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @Before
    public void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_ARTICLE WHERE F_ID = 1");
            statement.executeUpdate("INSERT INTO T_ARTICLE (F_ID, F_USER_ID, F_DATE, F_NAME, F_ANNOTATION," +
                    " F_TEXT, F_COUNT_VIEWS)" +
                    " VALUES (1, 2 , '2019-05-23 16:46:12', 'Article Name'," +
                    " 'ANNOTAtionenatatione antat', 'text text text text', 0)");
        }
    }

    @WithMockUser(authorities = {SECURE_REST_API})
    @Test
    public void shouldGetSucceedWith200ForGetArticlesApi() throws Exception {
        this.mockMvc.perform(get("/api/v1/articles"))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = "secure@mail.ru", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    public void shouldGetSucceedWith200ForAddArticleApi() throws Exception {
        this.mockMvc.perform(post("/api/v1/articles")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{" +
                        "\"date\": \"2019-05-23T16:46\"," +
                        "\"name\": \"Article nameasdasdasd\"," +
                        "\"annotation\": \"Annotation of article\"," +
                        "\"text\": \"Article text heredasdas\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {SECURE_REST_API})
    @Test
    public void shouldGetSucceedWith200ForShowArticleApi() throws Exception {
        this.mockMvc.perform(get("/api/v1/articles/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {SECURE_REST_API})
    @Test
    public void shouldGetSucceedWith200ForDeleteArticleApi() throws Exception {
        this.mockMvc.perform(delete("/api/v1/articles/1"))
                .andExpect(status().isOk());
    }
}
