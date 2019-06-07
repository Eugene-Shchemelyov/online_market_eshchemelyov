package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
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
public class ReviewControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @Before
    public void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_REVIEW WHERE F_USER_ID = 1");
            statement.executeUpdate("INSERT INTO T_REVIEW (F_ID, F_USER_ID, F_TEXT, F_DATE, F_IS_DISPLAY, F_IS_DELETED)" +
                    "VALUES (5, 1, 'text', NOW(), 1, 0)");
        }
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldToShowStatus200ForReviewsPage() throws Exception {
        this.mockMvc.perform(get("/private/reviews"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldToShowStatus302ForReviewsPage() throws Exception {
        this.mockMvc.perform(get("/private/reviews"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldRedirectAfterChangeReviewsDisplay() throws Exception {
        this.mockMvc.perform(post("/private/reviews/display"))
                .andExpect(redirectedUrl("/private/reviews"));
    }

    @Test
    public void shouldNotRedirectAfterChangeReviewsDisplay() throws Exception {
        this.mockMvc.perform(post("/private/reviews/display"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldRedirectAfterDeleteTheReview() throws Exception {
        this.mockMvc.perform(get("/private/reviews/5/delete"))
                .andExpect(redirectedUrl("/private/reviews?message=true"));
    }

    @Test
    public void shouldNotRedirectAfterDeleteTheReview() throws Exception {
        this.mockMvc.perform(get("/private/reviews/5/delete"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {CUSTOMER_USER})
    @Test
    public void shouldGetNewReviewPage() throws Exception {
        this.mockMvc.perform(get("/private/reviews/review/new"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotGetNewReviewPage() throws Exception {
        this.mockMvc.perform(get("/private/reviews/review/new"))
                .andExpect(status().isFound());
    }

    @WithUserDetails(value = "customer@mail.ru", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    public void shouldSendNewReview() throws Exception {
        this.mockMvc.perform(post("/private/reviews/review/new")
                .param("review", "test"))
                .andExpect(redirectedUrl("/private/items"));
    }

    @Test
    public void shouldNotSendNewReview() throws Exception {
        this.mockMvc.perform(post("/private/reviews/review/new")
                .param("review", "test"))
                .andExpect(status().isFound());
    }
}
