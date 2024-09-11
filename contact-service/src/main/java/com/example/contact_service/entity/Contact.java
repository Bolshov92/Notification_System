package com.example.contact_service.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    public Contact() {
    }

    public Contact(Long fileId, String name, String phoneNumber) {
        this.fileId = fileId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}