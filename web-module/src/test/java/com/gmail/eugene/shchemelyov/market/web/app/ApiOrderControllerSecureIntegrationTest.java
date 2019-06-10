package com.gmail.eugene.shchemelyov.market.web.app;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SECURE_REST_API;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiOrderControllerSecureIntegrationTest extends GenericControllerSecureIntegrationTest {
    @Before
    public void initialize() throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/project;user=test;password=test;");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM T_ORDER_ITEM");
            statement.executeUpdate("DELETE FROM T_ITEM WHERE F_ID = 10");
            statement.executeUpdate("DELETE FROM T_ITEM " +
                    "WHERE F_UNIQUE_NUMBER = '9c2e19c1-3d4b-44c6-a38e-e423e0138ba3'");
            statement.executeUpdate("DELETE FROM T_ORDER WHERE F_ID = 10");
            statement.executeUpdate("DELETE FROM T_ORDER WHERE F_UNIQUE_NUMBER = 1231233");
            statement.executeUpdate("INSERT INTO T_ITEM (F_ID, F_NAME, F_UNIQUE_NUMBER, F_PRICE, F_DESCRIPTION)" +
                    " VALUES (10, 'test', '9c2e19c1-3d4b-44c6-a38e-e423e0138ba3', '100', 'test')");
            statement.executeUpdate("INSERT INTO T_ORDER (F_ID, F_UNIQUE_NUMBER, F_STATUS," +
                    " F_COUNT_ITEMS, F_TOTAL_PRICE, F_USER_ID, F_DATE)" +
                    " VALUES (10, 1231233, 'NEW', 3, 300, 1, NOW())");
            statement.executeUpdate("INSERT INTO T_ORDER_ITEM (F_ORDER_ID, F_ITEM_ID) VALUES (10,10)");
        }
    }

    @WithMockUser(authorities = {SECURE_REST_API})
    @Test
    public void shouldGetSucceedWith200ForGetOrdersApi() throws Exception {
        this.mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = {SECURE_REST_API})
    @Test
    public void shouldGetSucceedWith200ForGetOrderApi() throws Exception {
        this.mockMvc.perform(get("/api/v1/orders/10"))
                .andExpect(status().isOk());
    }
}
