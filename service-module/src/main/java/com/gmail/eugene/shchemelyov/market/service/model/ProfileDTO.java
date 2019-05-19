package com.gmail.eugene.shchemelyov.market.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ProfileDTO {
    private UserDTO userDTO;
    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9.,!?\\s]{0,100}")
    private String address;
    @NotNull
    @Pattern(regexp = "\\+[0-9]{10,15}")
    private String phone;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
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
