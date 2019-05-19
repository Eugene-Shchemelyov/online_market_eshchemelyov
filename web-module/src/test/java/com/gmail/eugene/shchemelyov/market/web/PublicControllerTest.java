package com.gmail.eugene.shchemelyov.market.web;

import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PublicControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(PublicControllerTest.class);
    private MockMvc mockMvc;

    @Before
    public void initialize() {
        PublicController publicController = new PublicController();
        mockMvc = MockMvcBuilders.standaloneSetup(publicController).build();
    }

    @Test
    public void shouldGetLoginPageForLoginUrl() throws Exception {
        this.mockMvc.perform(get("/login.html"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("login"));
    }

    @Test
    public void getTimeStamp() throws Exception {
        logger.info(TimeStamp.getCurrentTime().toString());
        logger.info(TimeStamp.getCurrentTime().toString());
        logger.info(TimeStamp.getCurrentTime().toString());
    }
}
