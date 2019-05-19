package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.ProfileRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Profile;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ProfileRepositoryImpl extends GenericRepositoryImpl<Long, Profile> implements ProfileRepository {
    public ProfileRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }
}
