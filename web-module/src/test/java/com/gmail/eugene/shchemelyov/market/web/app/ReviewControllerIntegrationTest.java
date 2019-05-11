package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = {ADMINISTRATOR})
    public void shouldGetReviewsPage() throws Exception {
        this.mockMvc.perform(get("/private/administrator/reviews"))
                .andExpect(status().isOk());
    }
}
