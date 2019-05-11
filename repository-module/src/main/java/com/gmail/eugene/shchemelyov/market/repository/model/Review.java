package com.gmail.eugene.shchemelyov.market.repository.model;

public class Review {
    private Long id;
    private User user;
    private String text;
    private String date;
    private Boolean isDisplay;
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
}
