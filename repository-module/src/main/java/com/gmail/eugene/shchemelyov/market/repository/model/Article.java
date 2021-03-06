package com.gmail.eugene.shchemelyov.market.repository.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "T_ARTICLE")
@SQLDelete(sql = "UPDATE T_ARTICLE SET F_IS_DELETED = 1 WHERE F_ID = ?")
@Where(clause = "F_IS_DELETED = 0")
public class Article implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID", updatable = false, nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_USER_ID", nullable = false)
    private User user;
    @Column(name = "F_DATE", nullable = false)
    private String date;
    @Column(name = "F_NAME", nullable = false)
    private String name;
    @Column(name = "F_TEXT", nullable = false)
    private String text;
    @Column(name = "F_COUNT_VIEWS", nullable = false, insertable = false)
    private Long countViews;
    @OneToMany(
            mappedBy = "article",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();
    @Column(name = "F_IS_DELETED", nullable = false)
    private boolean isDeleted = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCountViews() {
        return countViews;
    }

    public void setCountViews(Long countViews) {
        this.countViews = countViews;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return isDeleted == article.isDeleted &&
                Objects.equals(id, article.id) &&
                Objects.equals(user, article.user) &&
                Objects.equals(date, article.date) &&
                Objects.equals(name, article.name) &&
                Objects.equals(text, article.text) &&
                Objects.equals(countViews, article.countViews) &&
                Objects.equals(comments, article.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, date, name, text, countViews, comments, isDeleted);
    }
}
