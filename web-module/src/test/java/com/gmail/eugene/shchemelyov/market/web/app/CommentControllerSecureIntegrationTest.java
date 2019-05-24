package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SALE_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @Before
    public void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_COMMENT WHERE F_ID = 10");
            statement.executeUpdate("DELETE FROM T_ARTICLE WHERE F_ID = 2");
            statement.executeUpdate("INSERT INTO T_ARTICLE (F_ID, F_USER_ID, F_DATE, F_NAME, F_ANNOTATION," +
                    " F_TEXT, F_COUNT_VIEWS)" +
                    " VALUES (2, 2 , '2019-05-23 16:46:12', 'Article Name', 'ANNOTA antat', 'text text', 0)");
            statement.executeUpdate("INSERT INTO T_COMMENT (F_ID, F_USER_ID, F_ARTICLE_ID, F_DATE, F_TEXT)" +
                    "VALUES (10, 1, 2, NOW(), 'comment text')");
        }
    }

    @WithMockUser(authorities = {SALE_USER})
    @Test
    public void shouldRedirectAfterDeleteTheComment() throws Exception {
        this.mockMvc.perform(get("/private/comments/10/delete")
                .param("articleId", "2"))
                .andExpect(redirectedUrl("/private/articles/2"));
    }

    @Test
    public void shouldNotRedirectAfterDeleteTheComment() throws Exception {
        this.mockMvc.perform(get("/private/comments/10/delete")
                .param("articleId", "2"))
                .andExpect(status().isFound());
    }
}
