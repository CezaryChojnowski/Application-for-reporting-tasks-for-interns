package com.exadel.model;

public class Contact {
    String phone;
    String email;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contact() {
    }

    public Contact(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }
}
