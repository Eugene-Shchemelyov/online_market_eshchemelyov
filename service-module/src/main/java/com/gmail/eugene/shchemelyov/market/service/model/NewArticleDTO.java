package com.gmail.eugene.shchemelyov.market.service.model;

import javax.validation.constraints.NotEmpty;

public class NewArticleDTO extends ArticleDTO {
    @NotEmpty
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
