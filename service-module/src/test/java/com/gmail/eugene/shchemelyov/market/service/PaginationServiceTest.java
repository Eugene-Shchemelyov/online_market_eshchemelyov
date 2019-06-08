package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.impl.PaginationServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaginationServiceTest extends GenericServiceTest {
    @Before
    public void initialize() {
        paginationService = new PaginationServiceImpl(
                userRepository,
                reviewRepository,
                articleRepository,
                itemRepository,
                orderRepository
        );
        pagination.setCurrentPage(1);
        pagination.setCountPages(5);
        pagination.setStartLimitPosition(0);
    }

    @Test
    public void shouldGetUserPagination() {
        when(userRepository.getCountOfEntities()).thenReturn(50);
        Pagination loadedPagination = paginationService.getUserPagination(1);
        Assert.assertEquals(pagination.getCurrentPage(), loadedPagination.getCurrentPage());
        Assert.assertEquals(pagination.getCountPages(), loadedPagination.getCountPages());
        Assert.assertEquals(pagination.getStartLimitPosition(), loadedPagination.getStartLimitPosition());
    }
}