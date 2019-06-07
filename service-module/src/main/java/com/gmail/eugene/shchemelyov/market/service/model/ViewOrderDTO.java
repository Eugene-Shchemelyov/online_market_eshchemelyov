package com.gmail.eugene.shchemelyov.market.service.model;

public class ViewOrderDTO extends OrderDTO {
    private String userSurname;
    private String userPhone;

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
