package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @WithUserDetails(value = "customer@mail.ru", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    public void shouldShowStatus200ForProfilePage() throws Exception {
        this.mockMvc.perform(get("/private/profile"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForProfilePage() throws Exception {
        this.mockMvc.perform(get("/private/profile"))
                .andExpect(status().isFound());
    }
}
