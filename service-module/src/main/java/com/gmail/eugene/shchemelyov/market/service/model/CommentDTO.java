package com.gmail.eugene.shchemelyov.market.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentDTO {
    private Long id;
    private UserDTO user;
    private String date;
    @NotNull
    @Size(max = 100)
    private String text;
    private boolean isDeleted = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
