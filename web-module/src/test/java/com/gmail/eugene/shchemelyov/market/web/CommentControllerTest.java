package com.gmail.eugene.shchemelyov.market.web;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.CommentService;
import com.gmail.eugene.shchemelyov.market.service.PaginationService;
import com.gmail.eugene.shchemelyov.market.service.model.CommentDTO;
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
public class CommentControllerTest {
    private MockMvc mockMvc;
    @Mock
    private CommentService commentService;
    @Mock
    private PaginationService paginationService;

    private CommentDTO commentDTO = new CommentDTO();
    private List<CommentDTO> comments = new ArrayList<>();
    private Pagination pagination = new Pagination();

    @Before
    public void initialize() {
        commentDTO.setId(1L);
        UserDTO userDTO = new UserDTO();
        userDTO.setSurname("Surname");
        userDTO.setName("Name");
        userDTO.setPatronymic("Patronymic");
        commentDTO.setUser(userDTO);
        commentDTO.setDate("Date");
        commentDTO.setDisplay(true);
        commentDTO.setText("Text");
        comments = asList(commentDTO, commentDTO, commentDTO);
        pagination.setCountPages(1);
        pagination.setCurrentPage(1);
        CommentController commentController = new CommentController(commentService, paginationService);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldGetCommentsPage() throws Exception {
        when(paginationService.getCommentPagination(null)).thenReturn(pagination);
        when(commentService.getComments(pagination)).thenReturn(comments);
        this.mockMvc.perform(get("/private/comments"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pagination", pagination))
                .andExpect(model().attribute("comments", comments))
                .andExpect(forwardedUrl("comment/allForAdministrator"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldGetCommentsPageWithNumberPageInUrl() throws Exception {
        when(paginationService.getCommentPagination(1)).thenReturn(pagination);
        when(commentService.getComments(pagination)).thenReturn(comments);
        this.mockMvc.perform(get("/private/comments/{page}", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pagination", pagination))
                .andExpect(model().attribute("comments", comments))
                .andExpect(forwardedUrl("comment/allForAdministrator"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldGetCommentsPageAndAddAttributeMessageToModel() throws Exception {
        when(paginationService.getCommentPagination(null)).thenReturn(pagination);
        when(commentService.getComments(pagination)).thenReturn(comments);
        this.mockMvc.perform(get("/private/comments?message=true"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("pagination", pagination))
                .andExpect(model().attribute("comments", comments))
                .andExpect(model().attribute("message", true))
                .andExpect(forwardedUrl("comment/allForAdministrator"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldRedirectToCommentsPageAfterChangingCommentDisplays() throws Exception {
        doNothing().when(commentService).changeCommentsDisplay(asList(1L, 2L));
        this.mockMvc.perform(post("/private/comments/display")
                .param("commentsId", "1")
                .param("commentsId", "2"))
                .andExpect(redirectedUrl("/private/comments"));
    }

    @WithMockUser(authorities = ADMINISTRATOR)
    @Test
    public void shouldRedirectToCommentsPageAfterDeletingComment() throws Exception {
        doNothing().when(commentService).delete(1L);
        this.mockMvc.perform(get("/private/comments/{id}/delete", "1"))
                .andExpect(redirectedUrl("/private/comments?message=true"));
    }
}
