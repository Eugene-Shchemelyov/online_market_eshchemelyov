package com.gmail.eugene.shchemelyov.market.service.converter;

import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.impl.ReviewConverterImpl;
import com.gmail.eugene.shchemelyov.market.service.model.ReviewDTO;
import com.gmail.eugene.shchemelyov.market.service.model.UserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;

@RunWith(MockitoJUnitRunner.class)
public class ReviewConverterTest {
    private ReviewConverter reviewConverter;

    private User user = new User();
    private UserDTO userDTO = new UserDTO();
    private Review review = new Review();
    private ReviewDTO reviewDTO = new ReviewDTO();

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

        review.setId(1L);
        review.setUser(user);
        review.setText("Text");
        review.setDate("Date");
        review.setDisplay(true);
        review.setDeleted(false);

        reviewDTO.setId(1L);
        reviewDTO.setUser(userDTO);
        reviewDTO.setText("Text");
        reviewDTO.setDate("Date");
        reviewDTO.setDisplay(true);
        reviewDTO.setDeleted(false);

        reviewConverter = new ReviewConverterImpl();
    }

    @Test
    public void shouldConvertedReviewIdToReviewDTOId() {
        ReviewDTO loadedReviewDTO = reviewConverter.toReviewDTO(review, userDTO);
        Assert.assertEquals(reviewDTO.getId(), loadedReviewDTO.getId());
    }

    @Test
    public void shouldConvertedReviewUserToReviewDTOUserDTO() {
        ReviewDTO loadedReviewDTO = reviewConverter.toReviewDTO(review, userDTO);
        Assert.assertEquals(reviewDTO.getUser(), loadedReviewDTO.getUser());
    }

    @Test
    public void shouldConvertedReviewTextToReviewDTOText() {
        ReviewDTO loadedReviewDTO = reviewConverter.toReviewDTO(review, userDTO);
        Assert.assertEquals(reviewDTO.getText(), loadedReviewDTO.getText());
    }

    @Test
    public void shouldConvertedReviewDateToReviewDTODate() {
        ReviewDTO loadedReviewDTO = reviewConverter.toReviewDTO(review, userDTO);
        Assert.assertEquals(reviewDTO.getDate(), loadedReviewDTO.getDate());
    }

    @Test
    public void shouldConvertedReviewDisplayToReviewDTODisplay() {
        ReviewDTO loadedReviewDTO = reviewConverter.toReviewDTO(review, userDTO);
        Assert.assertEquals(reviewDTO.isDisplay(), loadedReviewDTO.isDisplay());
    }

    @Test
    public void shouldConvertedReviewDeletedToReviewDTODeleted() {
        ReviewDTO loadedReviewDTO = reviewConverter.toReviewDTO(review, userDTO);
        Assert.assertEquals(reviewDTO.isDeleted(), loadedReviewDTO.isDeleted());
    }

    @Test
    public void shouldConvertedReviewDTOIdToReviewId() {
        Review loadedReview = reviewConverter.toReview(reviewDTO, user);
        Assert.assertEquals(review.getId(), loadedReview.getId());
    }

    @Test
    public void shouldConvertedReviewDTOUserDTOToReviewUser() {
        Review loadedReview = reviewConverter.toReview(reviewDTO, user);
        Assert.assertEquals(review.getUser(), loadedReview.getUser());
    }

    @Test
    public void shouldConvertedReviewDTOTextToReviewText() {
        Review loadedReview = reviewConverter.toReview(reviewDTO, user);
        Assert.assertEquals(review.getText(), loadedReview.getText());
    }

    @Test
    public void shouldConvertedReviewDTODateToReviewDate() {
        Review loadedReview = reviewConverter.toReview(reviewDTO, user);
        Assert.assertEquals(review.getDate(), loadedReview.getDate());
    }

    @Test
    public void shouldConvertedReviewDTODisplayToReviewDisplay() {
        Review loadedReview = reviewConverter.toReview(reviewDTO, user);
        Assert.assertEquals(review.isDisplay(), loadedReview.isDisplay());
    }

    @Test
    public void shouldConvertedReviewDTODeletedToReviewDeleted() {
        Review loadedReview = reviewConverter.toReview(reviewDTO, user);
        Assert.assertEquals(review.isDeleted(), loadedReview.isDeleted());
    }
}
