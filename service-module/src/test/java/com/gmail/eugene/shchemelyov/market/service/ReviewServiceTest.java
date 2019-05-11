package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.ReviewRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Review;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.service.converter.ReviewConverter;
import com.gmail.eugene.shchemelyov.market.service.converter.UserConverter;
import com.gmail.eugene.shchemelyov.market.service.impl.ReviewServiceImpl;
import com.gmail.eugene.shchemelyov.market.service.model.ReviewDTO;
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
public class ReviewServiceTest {
    private ReviewService reviewService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewConverter reviewConverter;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private Connection connection;

    private Pagination pagination = new Pagination();
    private User user = new User();
    private UserDTO userDTO = new UserDTO();
    private Review review = new Review();
    private ReviewDTO reviewDTO = new ReviewDTO();
    private List<Review> reviews = new ArrayList<>();
    private List<ReviewDTO> reviewDTOS = new ArrayList<>();

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

        reviews = asList(review, review, review);
        reviewDTOS = asList(reviewDTO, reviewDTO, reviewDTO);

        reviewService = new ReviewServiceImpl(reviewRepository, reviewConverter, userRepository, userConverter);
        when(reviewRepository.getConnection()).thenReturn(connection);
    }

    @Test
    public void shouldGetReviews() {
        when(reviewRepository.getLimitReviews(connection, pagination)).thenReturn(reviews);
        when(userRepository.loadUserById(connection, 2L)).thenReturn(user);
        when(userConverter.toUserDTO(user)).thenReturn(userDTO);
        when(reviewConverter.toReviewDTO(review, userDTO)).thenReturn(reviewDTO);

        List<ReviewDTO> loadedReviews = reviewService.getReviews(pagination);
        Assert.assertEquals(reviewDTOS, loadedReviews);
    }
}
