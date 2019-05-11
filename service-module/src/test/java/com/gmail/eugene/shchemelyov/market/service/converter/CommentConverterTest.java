package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Comment;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.impl.CommentConverterImpl;
import com.gmail.eugene.shchemelyov.market.service.model.CommentDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;

@RunWith(MockitoJUnitRunner.class)
public class CommentConverterTest {
    private CommentConverter commentConverter;

    private User user = new User();
    private UserDTO userDTO = new UserDTO();
    private Comment comment = new Comment();
    private CommentDTO commentDTO = new CommentDTO();

    @Before
    public void initialize() {
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

        commentConverter = new CommentConverterImpl();
    }

    @Test
    public void shouldConvertedCommentIdToCommentDTOId() {
        CommentDTO loadedCommentDTO = commentConverter.toCommentDTO(comment, userDTO);
        Assert.assertEquals(commentDTO.getId(), loadedCommentDTO.getId());
    }

    @Test
    public void shouldConvertedCommentUserToCommentDTOUserDTO() {
        CommentDTO loadedCommentDTO = commentConverter.toCommentDTO(comment, userDTO);
        Assert.assertEquals(commentDTO.getUser(), loadedCommentDTO.getUser());
    }

    @Test
    public void shouldConvertedCommentTextToCommentDTOText() {
        CommentDTO loadedCommentDTO = commentConverter.toCommentDTO(comment, userDTO);
        Assert.assertEquals(commentDTO.getText(), loadedCommentDTO.getText());
    }

    @Test
    public void shouldConvertedCommentDateToCommentDTODate() {
        CommentDTO loadedCommentDTO = commentConverter.toCommentDTO(comment, userDTO);
        Assert.assertEquals(commentDTO.getDate(), loadedCommentDTO.getDate());
    }

    @Test
    public void shouldConvertedCommentDisplayToCommentDTODisplay() {
        CommentDTO loadedCommentDTO = commentConverter.toCommentDTO(comment, userDTO);
        Assert.assertEquals(commentDTO.isDisplay(), loadedCommentDTO.isDisplay());
    }

    @Test
    public void shouldConvertedCommentDeletedToCommentDTODeleted() {
        CommentDTO loadedCommentDTO = commentConverter.toCommentDTO(comment, userDTO);
        Assert.assertEquals(commentDTO.isDeleted(), loadedCommentDTO.isDeleted());
    }

    @Test
    public void shouldConvertedCommentDTOIdToCommentId() {
        Comment loadedComment = commentConverter.toComment(commentDTO, user);
        Assert.assertEquals(comment.getId(), loadedComment.getId());
    }

    @Test
    public void shouldConvertedCommentDTOUserDTOToCommentUser() {
        Comment loadedComment = commentConverter.toComment(commentDTO, user);
        Assert.assertEquals(comment.getUser(), loadedComment.getUser());
    }

    @Test
    public void shouldConvertedCommentDTOTextToCommentText() {
        Comment loadedComment = commentConverter.toComment(commentDTO, user);
        Assert.assertEquals(comment.getText(), loadedComment.getText());
    }

    @Test
    public void shouldConvertedCommentDTODateToCommentDate() {
        Comment loadedComment = commentConverter.toComment(commentDTO, user);
        Assert.assertEquals(comment.getDate(), loadedComment.getDate());
    }

    @Test
    public void shouldConvertedCommentDTODisplayToCommentDisplay() {
        Comment loadedComment = commentConverter.toComment(commentDTO, user);
        Assert.assertEquals(comment.isDisplay(), loadedComment.isDisplay());
    }

    @Test
    public void shouldConvertedCommentDTODeletedToCommentDeleted() {
        Comment loadedComment = commentConverter.toComment(commentDTO, user);
        Assert.assertEquals(comment.isDeleted(), loadedComment.isDeleted());
    }
}
