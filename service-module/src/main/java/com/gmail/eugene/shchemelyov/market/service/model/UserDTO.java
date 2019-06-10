package com.gmail.eugene.shchemelyov.market.service.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.MAX_EMAIL_SIZE;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.NAME_PATTERN;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.PASSWORD_PATTERN;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.PATRONYMIC_PATTERN;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.SURNAME_PATTERN;

public class UserDTO {
    private Long id;
    @NotNull
    @Pattern(regexp = SURNAME_PATTERN)
    private String surname;
    @NotNull
    @Pattern(regexp = NAME_PATTERN)
    private String name;
    @NotNull
    @Pattern(regexp = PATRONYMIC_PATTERN)
    private String patronymic;
    @NotNull
    @Size(max = MAX_EMAIL_SIZE)
    @Email
    private String email;
    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;
    private RoleDTO role;

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

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
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
}
