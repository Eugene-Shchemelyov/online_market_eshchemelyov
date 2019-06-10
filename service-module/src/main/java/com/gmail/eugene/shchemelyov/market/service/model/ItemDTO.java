package com.gmail.eugene.shchemelyov.market.service.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateItemConstant.COUNT_FRACTIONS;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateItemConstant.COUNT_INTEGERS;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateItemConstant.NAME_PATTERN;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateItemConstant.UNIQUE_NUMBER_PATTERN;

public class ItemDTO {
    @NotNull
    @Pattern(regexp = NAME_PATTERN)
    private String name;
    @Pattern(regexp = UNIQUE_NUMBER_PATTERN)
    private String uniqueNumber;
    @NotNull
    @Digits(integer = COUNT_INTEGERS, fraction = COUNT_FRACTIONS)
    private BigDecimal price;

    public String getName() {
        return name;
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    @XmlElement(name = "uniqueNumber")
    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @XmlElement(name = "price")
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
