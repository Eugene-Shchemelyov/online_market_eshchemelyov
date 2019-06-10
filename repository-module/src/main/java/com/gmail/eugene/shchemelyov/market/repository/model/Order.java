package com.gmail.eugene.shchemelyov.market.repository.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "T_ORDER")
@SQLDelete(sql = "UPDATE T_ORDER SET F_IS_DELETED = 1 WHERE F_ID = ?")
@Where(clause = "F_IS_DELETED = 0")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID", updatable = false, nullable = false)
    private Long id;
    @Column(name = "F_UNIQUE_NUMBER", nullable = false)
    private Long uniqueNumber;
    @Column(name = "F_STATUS", nullable = false)
    private String status;
    @Column(name = "F_COUNT_ITEMS", nullable = false)
    private Integer countItems;
    @Column(name = "F_TOTAL_PRICE", nullable = false)
    private BigDecimal totalPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_USER_ID", nullable = false)
    private User user;
    @Column(name = "F_DATE", nullable = false)
    private String date;
    @ManyToMany
    @JoinTable(
            name = "T_ORDER_ITEM",
            joinColumns = {@JoinColumn(name = "F_ORDER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "F_ITEM_ID")}
    )
    private List<Item> items = new ArrayList<>();
    @Column(name = "F_IS_DELETED", nullable = false)
    private boolean isDeleted = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(Long uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCountItems() {
        return countItems;
    }

    public void setCountItems(Integer countItems) {
        this.countItems = countItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
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
        Order order = (Order) o;
        return isDeleted == order.isDeleted &&
                Objects.equals(id, order.id) &&
                Objects.equals(uniqueNumber, order.uniqueNumber) &&
                Objects.equals(status, order.status) &&
                Objects.equals(countItems, order.countItems) &&
                Objects.equals(totalPrice, order.totalPrice) &&
                Objects.equals(user, order.user) &&
                Objects.equals(date, order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uniqueNumber, status, countItems, totalPrice, user, date, isDeleted);
    }
}
