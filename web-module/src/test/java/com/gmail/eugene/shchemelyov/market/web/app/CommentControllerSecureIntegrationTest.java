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

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SALE_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @Before
    public void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_COMMENT WHERE F_ID = 100");
            statement.executeUpdate("DELETE FROM T_ARTICLE WHERE F_ID = 2");
            statement.executeUpdate("INSERT INTO T_ARTICLE (F_ID, F_USER_ID, F_DATE, F_NAME," +
                    " F_TEXT, F_COUNT_VIEWS)" +
                    " VALUES (2, 2 , '2019-05-23 16:46:12', 'Article Name', 'text text', 0)");
            statement.executeUpdate("INSERT INTO T_COMMENT (F_ID, F_USER_ID, F_ARTICLE_ID, F_DATE, F_TEXT)" +
                    "VALUES (100, 1, 2, NOW(), 'comment text')");
        }
    }

    @WithMockUser(authorities = {SALE_USER})
    @Test
    public void shouldRedirectAfterDeleteTheComment() throws Exception {
        this.mockMvc.perform(get("/private/comments/100/delete")
                .param("articleId", "2"))
                .andExpect(redirectedUrl("/private/articles/2"));
    }

    @Test
    public void shouldNotRedirectAfterDeleteTheComment() throws Exception {
        this.mockMvc.perform(get("/private/comments/100/delete")
                .param("articleId", "2"))
                .andExpect(status().isFound());
    }
}
