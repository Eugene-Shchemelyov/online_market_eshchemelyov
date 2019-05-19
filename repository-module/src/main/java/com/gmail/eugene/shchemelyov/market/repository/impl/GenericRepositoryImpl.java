package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.GenericRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {
    private static Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);
    private final DataSource dataSource;
    protected Class<T> entityClass;
    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl(DataSource dataSource) {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass()
                .getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[1];
        this.dataSource = dataSource;
    }

    @Override
    public void create(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public T getById(I id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public void deleteById(I id) {
        T entity = getById(id);
        delete(entity);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<T> getAllEntities() {
        String query = "FROM " + entityClass.getName() + " C";
        Query createdQuery = entityManager.createQuery(query);
        return createdQuery.getResultList();
    }

    @Override
    public Integer getCountOfEntities() {
        String query = "SELECT COUNT(*) FROM " + entityClass.getName() + " C";
        Query createdQuery = entityManager.createQuery(query);
        return ((Number) createdQuery.getSingleResult()).intValue();
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException("Problems with get connection", e);
        }
    }
}
