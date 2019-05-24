package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.ArticleRepository;
import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Article> getLimitArticles(Pagination pagination) {
        String orderByQuery = getOrderByQuery(pagination.getSort());
        String query = "FROM " + entityClass.getName() + " A JOIN A.user" + orderByQuery;
        Query createdQuery = entityManager.createQuery(query)
                .setFirstResult(pagination.getStartLimitPosition())
                .setMaxResults(pagination.getLimitOnPage());
        List objects = createdQuery.getResultList();
        return getArticles(objects);
    }

    private String getOrderByQuery(SortEnum sortEnum) {
        switch (sortEnum) {
            case DATE_DESC:
                return " ORDER BY A.date DESC";
            case DATE_ASC:
                return " ORDER BY A.date ASC";
            case USER_SURNAME_DESC:
                return " ORDER BY A.user.surname DESC";
            case USER_SURNAME_ASC:
                return " ORDER BY A.user.surname ASC";
            case VIEWS_DESC:
                return " ORDER BY A.countViews DESC";
            case VIEWS_ASC:
                return " ORDER BY A.countViews ASC";
            default:
                return " ORDER BY A.date DESC";
        }
    }

    private List<Article> getArticles(List objects) {
        List<Article> articles = new ArrayList<>();
        for (Object object : objects) {
            Object[] resultObject = (Object[]) object;
            Article article = (Article) resultObject[0];
            article.setUser((User) resultObject[1]);
            articles.add(article);
        }
        return articles;
    }
}
