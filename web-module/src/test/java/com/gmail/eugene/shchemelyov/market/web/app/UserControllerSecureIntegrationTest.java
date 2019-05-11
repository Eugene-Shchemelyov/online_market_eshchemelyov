package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerSecureIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldShowStatus200ForUsersPage() throws Exception {
        this.mockMvc.perform(get("/private/users"))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/private/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForUsersPage() throws Exception {
        this.mockMvc.perform(get("/private/users"))
                .andExpect(status().isFound());
        this.mockMvc.perform(get("/private/users/1"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldShowStatus200ForAddUserPage() throws Exception {
        this.mockMvc.perform(get("/private/users/add"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForAddUserPage() throws Exception {
        this.mockMvc.perform(get("/private/users/add"))
                .andExpect(status().isFound());
    }

    @WithMockUser(authorities = {ADMINISTRATOR})
    @Test
    public void shouldShowStatus200ForUpdateUserPage() throws Exception {
        this.mockMvc.perform(get("/private/users/1/update"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldShowStatus302ForUpdateUserPage() throws Exception {
        this.mockMvc.perform(get("/private/users/1/update"))
                .andExpect(status().isFound());
    }
}
