package com.gmail.eugene.shchemelyov.market.repository.impl;

import com.gmail.eugene.shchemelyov.market.repository.ArticleRepository;
import com.gmail.eugene.shchemelyov.market.repository.exception.RepositoryException;
import com.gmail.eugene.shchemelyov.market.repository.model.Article;
import com.gmail.eugene.shchemelyov.market.repository.model.Pagination;
import com.gmail.eugene.shchemelyov.market.repository.model.Role;
import com.gmail.eugene.shchemelyov.market.repository.model.User;
import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.repository.constant.ExceptionMessageConstant.REPOSITORY_ERROR_MESSAGE;

@Repository
public class ArticleRepositoryImpl extends GenericRepositoryImpl<Long, Article> implements ArticleRepository {
    private static final Logger logger = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

    public ArticleRepositoryImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Article> getLimitArticles(Connection connection, Pagination pagination) {
        String orderByQuery = getOrderByQuery(pagination.getSort());
        String query = "SELECT A.F_ID, A.F_DATE, A.F_NAME, A.F_ANNOTATION, A.F_TEXT, A.F_COUNT_VIEW," +
                " U.F_ID, U.F_NAME, U.F_SURNAME" +
                " FROM T_ARTICLE A " +
                " JOIN T_USER U ON U.F_ID = A.F_USER_ID" +
                " WHERE A.F_IS_DELETED = ? AND U.F_IS_DELETED = ?" +
                orderByQuery +
                " LIMIT ?, ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setBoolean(1, pagination.isDeleted());
            ps.setBoolean(2, pagination.isDeleted());
            ps.setInt(3, pagination.getStartLimitPosition());
            ps.setInt(4, pagination.getLimitOnPage());
            try (ResultSet resultSet = ps.executeQuery()) {
                List<Article> articles = new ArrayList<>();
                while (resultSet.next()) {
                    articles.add(getArticle(resultSet));
                }
                return articles;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format(
                    "%s. %s.", REPOSITORY_ERROR_MESSAGE, "When getting limit articles"), e);
        }
    }

    private String getOrderByQuery(SortEnum sortEnum) {
        switch (sortEnum) {
            case DATE_DESC:
                return " ORDER BY A.F_DATE DESC";
            case DATE_ASC:
                return " ORDER BY A.F_DATE ASC";
            case USER_SURNAME_DESC:
                return " ORDER BY U.F_SURNAME DESC";
            case USER_SURNAME_ASC:
                return " ORDER BY U.F_SURNAME ASC";
            case VIEWS_DESC:
                return " ORDER BY A.F_COUNT_VIEW DESC";
            case VIEWS_ASC:
                return " ORDER BY A.F_COUNT_VIEW ASC";
            default:
                return " ORDER BY A.F_DATE DESC";
        }
    }

    private Article getArticle(ResultSet resultSet) throws SQLException {
        Article article = new Article();
        article.setId(resultSet.getLong("A.F_ID"));
        article.setUser(getUser(resultSet));
        article.setDate(resultSet.getTimestamp("A.F_DATE").toString());
        article.setName(resultSet.getString("A.F_NAME"));
        article.setAnnotation(resultSet.getString("A.F_ANNOTATION"));
        article.setText(resultSet.getString("A.F_TEXT"));
        article.setCountViews(resultSet.getLong("A.F_COUNT_VIEW"));
        return article;
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("U.F_ID"));
        user.setName(resultSet.getString("U.F_NAME"));
        user.setSurname(resultSet.getString("U.F_SURNAME"));
        user.setRole(new Role());
        return user;
    }
}
