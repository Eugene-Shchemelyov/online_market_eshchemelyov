package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.CommentRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Comment;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.CommentConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.UserConverter;
import com.gmail.eugene.shchemelyov.market.service.impl.CommentServiceImpl;
import com.gmail.eugene.shchemelyov.market.service.model.CommentDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentConverter commentConverter;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private Connection connection;

    private Pagination pagination = new Pagination();
    private User user = new User();
    private UserDTO userDTO = new UserDTO();
    private Comment comment = new Comment();
    private CommentDTO commentDTO = new CommentDTO();
    private List<Comment> comments = new ArrayList<>();
    private List<CommentDTO> commentDTOs = new ArrayList<>();

    @Before
    public void initialize() {
        pagination.setCountPages(1);
        pagination.setCurrentPage(1);

        user.setId(2L);
        user.setSurname("Surname");
        user.setName("Name");
        user.setPatronymic("Patronymic");
        user.setEmail("Email");
        user.setPassword("1");
        Role roleFromDatabase = new Role();
        roleFromDatabase.setId(1L);
        user.setRole(roleFromDatabase);
        user.setDeleted(false);

        userDTO.setId(2L);
        userDTO.setSurname("Surname");
        userDTO.setName("Name");
        userDTO.setPatronymic("Patronymic");
        userDTO.setEmail("Email");
        userDTO.setPassword("1");
        userDTO.setRoleName(ADMINISTRATOR);
        userDTO.setDeleted(false);

        comment.setId(1L);
        comment.setUser(user);
        comment.setText("Text");
        comment.setDate("Date");
        comment.setDisplay(true);
        comment.setDeleted(false);

        commentDTO.setId(1L);
        commentDTO.setUser(userDTO);
        commentDTO.setText("Text");
        commentDTO.setDate("Date");
        commentDTO.setDisplay(true);
        commentDTO.setDeleted(false);

        comments = asList(comment, comment, comment);
        commentDTOs = asList(commentDTO, commentDTO, commentDTO);

        commentService = new CommentServiceImpl(commentRepository, commentConverter, userRepository, userConverter);
        when(commentRepository.getConnection()).thenReturn(connection);
    }

    @Test
    public void shouldGetComments() {
        when(commentRepository.getLimitComments(connection, pagination)).thenReturn(comments);
        when(userRepository.loadUserById(connection, 2L)).thenReturn(user);
        when(userConverter.toUserDTO(user)).thenReturn(userDTO);
        when(commentConverter.toCommentDTO(comment, userDTO)).thenReturn(commentDTO);

        List<CommentDTO> loadedComments = commentService.getComments(pagination);
        Assert.assertEquals(commentDTOs, loadedComments);
    }
}
