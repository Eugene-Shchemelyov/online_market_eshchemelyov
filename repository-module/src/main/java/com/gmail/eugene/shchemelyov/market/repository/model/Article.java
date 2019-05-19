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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "T_ARTICLE")
@SQLDelete(sql = "UPDATE T_ARTICLE SET F_IS_DELETED = 1 WHERE F_ID = ?")
@Where(clause = "F_IS_DELETED = 0")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID", updatable = false, nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_USER_ID", nullable = false)
    private User user = new User();
    @Column(name = "F_DATE")
    private String date;
    @Column(name = "F_NAME")
    private String name;
    @Column(name = "F_ANNOTATION")
    private String annotation;
    @Column(name = "F_TEXT")
    private String text;
    @Column(name = "F_COUNT_VIEW")
    private Long countViews;
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "F_ARTICLE_ID")
    private List<Comment> comments = new ArrayList<>();
    @Column(name = "F_IS_DELETED")
    private boolean isDeleted;

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

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
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
                Objects.equals(annotation, article.annotation) &&
                Objects.equals(text, article.text) &&
                Objects.equals(countViews, article.countViews) &&
                Objects.equals(comments, article.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, date, name, annotation, text, countViews, comments, isDeleted);
    }
}
