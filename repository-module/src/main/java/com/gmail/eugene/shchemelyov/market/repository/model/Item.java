package com.gmail.eugene.shchemelyov.market.repository.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "T_ITEM")
@SQLDelete(sql = "UPDATE T_ITEM SET F_IS_DELETED = 1 WHERE F_ID = ?")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID", nullable = false, updatable = false)
    private Long id;
    @Column(name = "F_NAME", nullable = false)
    private String name;
    @Column(name = "F_UNIQUE_NUMBER", nullable = false)
    private String uniqueNumber;
    @Column(name = "F_PRICE", nullable = false)
    private BigDecimal price;
    @Column(name = "F_DESCRIPTION", nullable = false)
    private String description;
    @Column(name = "F_IS_DELETED", nullable = false)
    private boolean isDeleted = false;

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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(name, item.name) &&
                Objects.equals(uniqueNumber, item.uniqueNumber) &&
                Objects.equals(price, item.price) &&
                Objects.equals(description, item.description) &&
                Objects.equals(isDeleted, item.isDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, uniqueNumber, price, description, isDeleted);
    }
}
