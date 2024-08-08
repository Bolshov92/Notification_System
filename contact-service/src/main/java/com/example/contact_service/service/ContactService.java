package com.example.contact_service.service;

import com.example.contact_service.entity.Contact;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ContactService {
    List<Contact> readContactsFromFile(MultipartFile file) throws IOException;
}
