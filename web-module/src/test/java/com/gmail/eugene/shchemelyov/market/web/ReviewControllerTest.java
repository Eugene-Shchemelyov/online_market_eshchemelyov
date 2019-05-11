package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.ReviewService;
import com.gmail.eugene.shchemelyov.market.service.model.ReviewDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ReviewControllerTest {
    private MockMvc mockMvc;
    @Mock
    private ReviewService reviewService;
    @Mock
    private PaginationService paginationService;

    private ReviewDTO reviewDTO = new ReviewDTO();
    private List<ReviewDTO> reviews = new ArrayList<>();
    private Pagination pagination = new Pagination();

    @Before
    public void initialize() {
        reviewDTO.setId(1L);
        UserDTO userDTO = new UserDTO();
        userDTO.setSurname("Surname");
        userDTO.setName("Name");
        userDTO.setPatronymic("Patronymic");
        reviewDTO.setUser(userDTO);
        reviewDTO.setDate("Date");
        reviewDTO.setDisplay(true);
        reviewDTO.setText("Text");
        reviews = asList(reviewDTO, reviewDTO, reviewDTO);
        pagination.setCountPages(1);
        pagination.setCurrentPage(1);
        ReviewController reviewController = new ReviewController(reviewService, paginationService);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldGetReviewsPage() throws Exception {
        when(paginationService.getReviewPagination(1)).thenReturn(pagination);
        when(reviewService.getReviews(pagination)).thenReturn(reviews);
        this.mockMvc.perform(get("/private/administrator/reviews"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pagination", pagination))
                .andExpect(model().attribute("reviews", reviews))
                .andExpect(forwardedUrl("review/allForAdministrator"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldGetReviewsPageAndAddAttributeMessageToModel() throws Exception {
        when(paginationService.getReviewPagination(1)).thenReturn(pagination);
        when(reviewService.getReviews(pagination)).thenReturn(reviews);
        this.mockMvc.perform(get("/private/administrator/reviews?message=true"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pagination", pagination))
                .andExpect(model().attribute("reviews", reviews))
                .andExpect(model().attribute("message", true))
                .andExpect(forwardedUrl("review/allForAdministrator"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldRedirectToReviewsPageAfterChangingReviewDisplays() throws Exception {
        doNothing().when(reviewService).changeReviewsDisplay(asList(1L, 2L));
        this.mockMvc.perform(post("/private/administrator/reviews/display")
                .param("reviewsId", "1")
                .param("reviewsId", "2"))
                .andExpect(redirectedUrl("/private/administrator/reviews"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldRedirectToReviewsPageAfterDeletingReview() throws Exception {
        doNothing().when(reviewService).delete(1L);
        this.mockMvc.perform(get("/private/administrator/reviews/delete?id=1"))
                .andExpect(redirectedUrl("/private/administrator/reviews?message=true"));
    }
}
