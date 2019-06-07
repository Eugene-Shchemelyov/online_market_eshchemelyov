package com.gmail.eugene.shchemelyov.market.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.ADDRESS_PATTERN;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.NAME_PATTERN;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.PASSWORD_PATTERN;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.PHONE_PATTERN;
import static com.gmail.eugene.shchemelyov.market.service.constant.validate.ValidateUserConstant.SURNAME_PATTERN;

public class UpdateUserDTO {
    @NotNull
    @Pattern(regexp = SURNAME_PATTERN)
    private String surname;
    @NotNull
    @Pattern(regexp = NAME_PATTERN)
    private String name;
    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;
    @Pattern(regexp = ADDRESS_PATTERN)
    private String address;
    @Pattern(regexp = PHONE_PATTERN)
    private String phone;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
