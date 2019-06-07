package com.gmail.eugene.shchemelyov.market.service.model;

import java.util.ArrayList;
import java.util.List;

public class ViewArticleDTO extends NewArticleDTO {
    private Long id;
    private String userName;
    private String userSurname;
    private Long countViews;
    private Integer countComments;
    private List<CommentDTO> comments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public Long getCountViews() {
        return countViews;
    }

    public void setCountViews(Long countViews) {
        this.countViews = countViews;
    }

    public Integer getCountComments() {
        return countComments;
    }

    public void setCountComments(Integer countComments) {
        this.countComments = countComments;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}
