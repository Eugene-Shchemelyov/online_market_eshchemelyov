package com.gmail.eugene.shchemelyov.market.service;

import com.gmail.eugene.shchemelyov.market.repository.RoleRepository;
import com.gmail.eugene.shchemelyov.market.service.impl.RoleServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {
    private RoleService roleService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private Connection connection;

    private List<String> roles = asList("Admin", "Admin2");

    @Test
    public void shouldReturnRoles() {
        when(roleRepository.getConnection()).thenReturn(connection);
        when(roleRepository.getAllRoles(connection)).thenReturn(roles);
        roleService = new RoleServiceImpl(roleRepository);

        List<String> loadedRoles = roleService.getAllRoles();
        Assert.assertEquals(roles, loadedRoles);
    }
}
