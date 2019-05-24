package com.gmail.eugene.shchemelyov.market.service.model;

import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class ItemDTO {
    private Long id;
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9.,!?:;\\s]{0,100}")
    private String name;
    @Pattern(regexp = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$")
    private String uniqueNumber;
    @NotNull
    @NumberFormat(pattern = "#,###,###,###.##")
    private BigDecimal price;
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9.,!?:;\\s]{0,200}")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
