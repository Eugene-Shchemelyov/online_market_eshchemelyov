package com.gmail.eugene.shchemelyov.market.service.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO {
    private Long id;
    @NotNull
    @Pattern(regexp = "[a-zA-Z]{0,40}")
    private String surname;
    @NotNull
    @Pattern(regexp = "[a-zA-Z]{0,20}")
    private String name;
    @NotNull
    @Size(max = 50)
    @Email
    private String email;
    @Pattern(regexp = "[a-zA-Z0-9]{8,30}")
    private String password;
    private RoleDTO role;
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
