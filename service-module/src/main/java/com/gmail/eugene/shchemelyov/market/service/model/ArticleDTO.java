package com.gmail.eugene.shchemelyov.market.service.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateArticleConstant.NAME_MAX_SIZE;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateArticleConstant.TEXT_MAX_SIZE;

public class ArticleDTO {
    @NotNull
    @NotEmpty
    @Size(max = NAME_MAX_SIZE)
    private String name;
    @NotNull
    @NotEmpty
    @Size(max = TEXT_MAX_SIZE)
    private String text;

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
}
