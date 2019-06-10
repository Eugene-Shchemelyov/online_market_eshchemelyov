package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @WithUserDetails(value = "customer@mail.ru", userDetailsServiceBeanName = "userDetailsServiceImpl")
    @Test
    public void shouldUpdateProfile() throws Exception {
        this.mockMvc.perform(post("/private/profile/update")
                .param("oldPassword", "")
                .param("newPassword", "")
                .param("surname", "test")
                .param("name", "test")
                .param("address", "test")
                .param("phone", "+345343434"))
                .andExpect(redirectedUrl("/private/profile"));
    }

    @Test
    public void shouldNotUpdateProfile() throws Exception {
        this.mockMvc.perform(post("/private/profile/update")
                .param("oldPassword", "")
                .param("newPassword", "")
                .param("surname", "test")
                .param("name", "test")
                .param("address", "test")
                .param("phone", "+345343434"))
                .andExpect(status().isFound());
    }
}
