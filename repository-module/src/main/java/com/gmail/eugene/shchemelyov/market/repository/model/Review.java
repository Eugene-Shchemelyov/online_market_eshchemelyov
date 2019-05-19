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
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "T_REVIEW")
@SQLDelete(sql = "UPDATE T_REVIEW SET F_IS_DELETED = 1 WHERE F_ID = ?")
@Where(clause = "F_IS_DELETED = 0")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID", updatable = false, nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "F_USER_ID")
    private User user;
    @Column(name = "F_TEXT")
    private String text;
    @Column(name = "F_DATE")
    private String date;
    @Column(name = "F_IS_DISPLAY")
    private Boolean isDisplay;
    @Column(name = "F_IS_DELETED")
    private Boolean isDeleted;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(Boolean display) {
        isDisplay = display;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) &&
                Objects.equals(user, review.user) &&
                Objects.equals(text, review.text) &&
                Objects.equals(date, review.date) &&
                Objects.equals(isDisplay, review.isDisplay) &&
                Objects.equals(isDeleted, review.isDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, text, date, isDisplay, isDeleted);
    }
}
