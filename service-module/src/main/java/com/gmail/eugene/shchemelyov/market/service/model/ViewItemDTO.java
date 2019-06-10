package com.gmail.eugene.shchemelyov.market.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateItemConstant.DESCRIPTION_PATTERN;

public class ViewItemDTO extends PreviewItemDTO {
    @NotNull
    @Pattern(regexp = DESCRIPTION_PATTERN)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
