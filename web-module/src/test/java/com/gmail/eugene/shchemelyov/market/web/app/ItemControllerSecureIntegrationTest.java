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

public class ItemControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @Before
    public void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_ITEM WHERE F_ID = 1");
            statement.executeUpdate("INSERT INTO T_ITEM (F_ID, F_NAME, F_UNIQUE_NUMBER, F_PRICE, F_DESCRIPTION)" +
                    " VALUES (1, 'test', '975f837b-0436-49d2-8107-b4d8b4287b82', '100', 'test')");
        }
    }

    @WithMockUser(authorities = {SALE_USER})
    @Test
    public void shouldToShowStatus200ForItemsPageForSaleUser() throws Exception {
        this.mockMvc.perform(get("/private/items"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldToShowStatus302ForItemsPage() throws Exception {
        this.mockMvc.perform(get("/private/items"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {SALE_USER})
    @Test
    public void shouldToShowStatus200ForItemPageForSaleUser() throws Exception {
        this.mockMvc.perform(get("/private/items/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldToShowStatus302ForItemPage() throws Exception {
        this.mockMvc.perform(get("/private/items/1"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {SALE_USER})
    @Test
    public void shouldRedirectToItemsPageAfterCopyItemForSaleUser() throws Exception {
        this.mockMvc.perform(get("/private/items/1/copy"))
                .andExpect(redirectedUrl("/private/items"));
    }

    @Test
    public void shouldNotRedirectToItemsPageAfterCopyItem() throws Exception {
        this.mockMvc.perform(get("/private/items/1/copy"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {SALE_USER})
    @Test
    public void shouldRedirectToItemsPageAfterDeletingForSaleUser() throws Exception {
        this.mockMvc.perform(get("/private/items/1/delete"))
                .andExpect(redirectedUrl("/private/items"));
    }

    @Test
    public void shouldNotRedirectToItemsPageAfterDeleting() throws Exception {
        this.mockMvc.perform(get("/private/items/1/delete"))
                .andExpect(status().isFound());
    }
}
