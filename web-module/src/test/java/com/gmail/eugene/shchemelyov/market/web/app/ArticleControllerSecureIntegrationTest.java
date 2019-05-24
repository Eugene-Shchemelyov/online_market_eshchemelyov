package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.CUSTOMER_USER;
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SALE_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @Before
    public void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_ARTICLE WHERE F_ID = 1");
            statement.executeUpdate("INSERT INTO T_ARTICLE (F_ID, F_USER_ID, F_DATE, F_NAME, F_ANNOTATION," +
                    " F_TEXT, F_COUNT_VIEWS)" +
                    " VALUES (1, 2 , '2019-05-23 16:46:12', 'Article Name', 'ANNOTA antat', 'text text', 0)");
        }
    }

    @WithMockUser(authorities = {CUSTOMER_USER})
    @Test
    public void shouldShowStatus200ForArticlesPageForCustomerUser() throws Exception {
        this.mockMvc.perform(get("/private/articles"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {SALE_USER})
    @Test
    public void shouldShowStatus200ForArticlesPageForSaleUser() throws Exception {
        this.mockMvc.perform(get("/private/articles"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForArticlesPage() throws Exception {
        this.mockMvc.perform(get("/private/articles"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {CUSTOMER_USER})
    @Test
    public void shouldShowStatus200ForArticlePageForCustomerUser() throws Exception {
        this.mockMvc.perform(get("/private/articles/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {SALE_USER})
    @Test
    public void shouldShowStatus200ForArticlePageForSaleUser() throws Exception {
        this.mockMvc.perform(get("/private/articles/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForArticlePage() throws Exception {
        this.mockMvc.perform(get("/private/articles/1"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {SALE_USER})
    @Test
    public void shouldShowStatus200ForUpdateArticlePageForSaleUser() throws Exception {
        this.mockMvc.perform(get("/private/articles/1/update"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {CUSTOMER_USER})
    @Test
    public void shouldShowStatus302ForUpdateArticlePageForCustomerUser() throws Exception {
        this.mockMvc.perform(get("/private/articles/1/update"))
                .andExpect(status().isFound());
    }

    @Test
    public void shouldShowStatus302ForUpdateArticlePage() throws Exception {
        this.mockMvc.perform(get("/private/articles/1/update"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {SALE_USER})
    @Test
    public void shouldRedirectToUpdateArticlePageAfterUpdateForSaleUser() throws Exception {
        this.mockMvc.perform(post("/private/articles/1/update")
                .param("text", "text")
                .param("annotation", "annotation")
                .param("name", "name"))
                .andExpect(redirectedUrl("/private/articles"));
    }

    @WithMockUser(authorities = {CUSTOMER_USER})
    @Test
    public void shouldNotRedirectToUpdateArticlePageAfterUpdateForCustomerUser() throws Exception {
        this.mockMvc.perform(post("/private/articles/1/update")
                .param("text", "text")
                .param("annotation", "annotation")
                .param("name", "name"))
                .andExpect(status().isFound());
    }

    @Test
    public void shouldNotRedirectToUpdateArticlePageAfterUpdate() throws Exception {
        this.mockMvc.perform(post("/private/articles/1/update")
                .param("text", "text")
                .param("annotation", "annotation")
                .param("name", "name"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {SALE_USER})
    @Test
    public void shouldShowStatus200ForNewArticlePageForSaleUser() throws Exception {
        this.mockMvc.perform(get("/private/articles/article/new"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {CUSTOMER_USER})
    @Test
    public void shouldShowStatus302ForNewArticlePageForCustomerUser() throws Exception {
        this.mockMvc.perform(get("/private/articles/article/new"))
                .andExpect(status().isFound());
    }

    @Test
    public void shouldShowStatus302ForNewArticlePage() throws Exception {
        this.mockMvc.perform(get("/private/articles/article/new"))
                .andExpect(status().isFound());
    }

    @WithUserDetails(value = "sale@mail.ru", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    public void shouldRedirectToArticlesPageAfterCreateArticleForSaleUser() throws Exception {
        this.mockMvc.perform(post("/private/articles/article/new")
                .param("date", "1111-11-11T11:11")
                .param("text", "text")
                .param("annotation", "annotation")
                .param("name", "name"))
                .andExpect(redirectedUrl("/private/articles"));
    }

    @WithUserDetails(value = "customer@mail.ru", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    public void shouldNotRedirectToArticlesPageAfterCreateArticleForCustomerUser() throws Exception {
        this.mockMvc.perform(post("/private/articles/article/new")
                .param("date", "1111-11-11T11:11")
                .param("text", "text")
                .param("annotation", "annotation")
                .param("name", "name"))
                .andExpect(status().isFound());
    }

    @Test
    public void shouldNotRedirectToArticlesPageAfterCreateArticle() throws Exception {
        this.mockMvc.perform(post("/private/articles/article/new")
                .param("date", "1111-11-11T11:11")
                .param("text", "text")
                .param("annotation", "annotation")
                .param("name", "name"))
                .andExpect(status().isFound());
    }
}
