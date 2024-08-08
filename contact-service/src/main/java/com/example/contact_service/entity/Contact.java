package com.example.contact_service.entity;

import lombok.Data;


@Data
public class Contact {

    private Long id;
    private String name;
    private String phoneNumber;

    public Contact() {
    }

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

}
