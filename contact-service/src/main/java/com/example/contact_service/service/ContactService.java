package com.emergency.service;

import com.emergency.entity.Contact;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ContactService {
    List<Contact> readContactsFromFile(MultipartFile file) throws IOException;
}
