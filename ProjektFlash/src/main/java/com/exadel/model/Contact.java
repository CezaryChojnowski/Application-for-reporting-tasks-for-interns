package com.exadel.model;

public class Contact {
    String phone;
    String email;

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Contact(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }
}
