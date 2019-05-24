package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.ArticleRepository;
import com.gmail.eugene.shchemelyov.market.repository.ItemRepository;
import com.gmail.eugene.shchemelyov.market.repository.ReviewRepository;
import com.gmail.eugene.shchemelyov.market.repository.UserRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;

@RunWith(MockitoJUnitRunner.class)
public class GenericServiceTest {
    protected PaginationService paginationService;
    @Mock
    protected UserRepository userRepository;
    @Mock
    protected ReviewRepository reviewRepository;
    @Mock
    protected ArticleRepository articleRepository;
    @Mock
    protected ItemRepository itemRepository;

    protected Pagination pagination = new Pagination();
}
