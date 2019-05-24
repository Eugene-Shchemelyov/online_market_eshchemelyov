package com.gmail.eugene.shchemelyov.market.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ArticleDTO {
    private Long id;
    private UserDTO user = new UserDTO();
    private String date;
    @NotNull
    @Size(max = 100)
    private String name;
    @NotNull
    @Size(max = 200)
    private String annotation;
    @NotNull
    @Size(max = 1000)
    private String text;
    private Long countViews;
    private List<CommentDTO> comments = new ArrayList<>();

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

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}
