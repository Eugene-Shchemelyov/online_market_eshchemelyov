package com.gmail.eugene.shchemelyov.market.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateItemConstant.DESCRIPTION_PATTERN;

@XmlRootElement(name = "item")
public class NewItemDTO extends ItemDTO {
    @NotNull
    @Pattern(regexp = DESCRIPTION_PATTERN)
    private String description;

    public String getDescription() {
        return description;
    }

    @XmlElement(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }
}
