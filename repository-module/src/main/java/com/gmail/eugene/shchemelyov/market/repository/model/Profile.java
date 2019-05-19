package com.gmail.eugene.shchemelyov.market.repository.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "T_PROFILE")
@SQLDelete(sql = "UPDATE T_PROFILE SET F_IS_DELETED = 1 WHERE F_USER_ID = ?")
@Where(clause = "F_IS_DELETED = 0")
public class Profile {
    @Id
    @GenericGenerator(
            name = "generator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "user")
    )
    @GeneratedValue(generator = "generator")
    @Column(name = "F_USER_ID", unique = true, nullable = false)
    private Long userId;
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private User user;
    @Column(name = "F_ADDRESS")
    private String address;
    @Column(name = "F_PHONE")
    private String phone;
    @Column(name = "F_IS_DELETED")
    private boolean isDeleted;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        Profile profile = (Profile) o;
        return isDeleted == profile.isDeleted &&
                Objects.equals(userId, profile.userId) &&
                Objects.equals(user, profile.user) &&
                Objects.equals(address, profile.address) &&
                Objects.equals(phone, profile.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, user, address, phone, isDeleted);
    }
}
