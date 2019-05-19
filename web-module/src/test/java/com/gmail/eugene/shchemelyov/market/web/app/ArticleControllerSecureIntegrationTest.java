package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.CUSTOMER_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @WithMockUser(authorities = {CUSTOMER_USER})
    @Test
    public void shouldShowStatus200ForArticlesPage() throws Exception {
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
    public void shouldShowStatus200ForArticlePage() throws Exception {
        this.mockMvc.perform(get("/private/articles/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForArticlePage() throws Exception {
        this.mockMvc.perform(get("/private/articles/1"))
                .andExpect(status().isFound());
    }
}
