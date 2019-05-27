package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.ArticleRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Article> getLimitArticles(Pagination pagination) {
        String orderByQuery = getOrderByQuery(pagination.getSort());
        String query = "FROM " + entityClass.getName() + orderByQuery;
        Query createdQuery = entityManager.createQuery(query)
                .setFirstResult(pagination.getStartLimitPosition())
                .setMaxResults(pagination.getLimitOnPage());
        return createdQuery.getResultList();
    }

    private String getOrderByQuery(SortEnum sortEnum) {
        switch (sortEnum) {
            case DATE_DESC:
                return " ORDER BY date DESC";
            case DATE_ASC:
                return " ORDER BY date ASC";
            case USER_SURNAME_DESC:
                return " ORDER BY user.surname DESC";
            case USER_SURNAME_ASC:
                return " ORDER BY user.surname ASC";
            case VIEWS_DESC:
                return " ORDER BY countViews DESC";
            case VIEWS_ASC:
                return " ORDER BY countViews ASC";
            default:
                return " ORDER BY date DESC";
        }
    }
}
