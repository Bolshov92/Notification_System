package com.emergency.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class Contact {
    private Long id;
    private String name;
    private String phoneNumber;

    public Contact(String s, String s1) {
    }
}
