package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.PaginationRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.service.impl.PaginationServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;

import static com.gmail.eugene.shchemelyov.market.repository.constant.PaginationConstant.LIMIT_ON_PAGE;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaginationServiceTest {
    private PaginationService paginationService;
    @Mock
    private PaginationRepository paginationRepository;
    @Mock
    private Connection connection;

    private Pagination pagination = new Pagination();

    @Before
    public void initialize() {
        paginationService = new PaginationServiceImpl(paginationRepository);
        when(paginationRepository.getConnection()).thenReturn(connection);
    }

    @Test
    public void shouldGetPaginationWithOnePage() {
        when(paginationRepository.getCountRaws(connection, "T_USER", false)).thenReturn(9);
        pagination.setCurrentPage(1);
        pagination.setCountPages(1);

        Pagination loadedPagination = paginationService.getUserPagination(1);
        Assert.assertEquals(pagination.getCurrentPage(), loadedPagination.getCurrentPage());
        Assert.assertEquals(pagination.getCountPages(), loadedPagination.getCountPages());
    }

    @Test
    public void shouldGetPaginationWithOnePageIfZeroRaws() {
        when(paginationRepository.getCountRaws(connection, "T_USER", false)).thenReturn(0);
        pagination.setCurrentPage(1);
        pagination.setCountPages(1);

        Pagination loadedPagination = paginationService.getUserPagination(1);
        Assert.assertEquals(pagination.getCurrentPage(), loadedPagination.getCurrentPage());
        Assert.assertEquals(pagination.getCountPages(), loadedPagination.getCountPages());
    }

    @Test
    public void shouldGetPaginationWithOnePageIfCountRawsEqualsLimit() {
        when(paginationRepository.getCountRaws(connection, "T_USER", false)).thenReturn(LIMIT_ON_PAGE);
        pagination.setCurrentPage(1);
        pagination.setCountPages(1);

        Pagination loadedPagination = paginationService.getUserPagination(1);
        Assert.assertEquals(pagination.getCurrentPage(), loadedPagination.getCurrentPage());
        Assert.assertEquals(pagination.getCountPages(), loadedPagination.getCountPages());
    }

    @Test
    public void shouldGetPaginationWithOnePageIfPageIsNull() {
        when(paginationRepository.getCountRaws(connection, "T_USER", false)).thenReturn(1);
        pagination.setCurrentPage(1);
        pagination.setCountPages(1);

        Pagination loadedPagination = paginationService.getUserPagination(null);
        Assert.assertEquals(pagination.getCurrentPage(), loadedPagination.getCurrentPage());
        Assert.assertEquals(pagination.getCountPages(), loadedPagination.getCountPages());
    }

    @Test
    public void shouldGetPaginationWithOnePageIfPageMoreThanCountPages() {
        when(paginationRepository.getCountRaws(connection, "T_USER", false)).thenReturn(1);
        pagination.setCurrentPage(1);
        pagination.setCountPages(1);

        Pagination loadedPagination = paginationService.getUserPagination(10000);
        Assert.assertEquals(pagination.getCurrentPage(), loadedPagination.getCurrentPage());
        Assert.assertEquals(pagination.getCountPages(), loadedPagination.getCountPages());
    }
}
