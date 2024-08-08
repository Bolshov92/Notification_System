package com.example.file_service.service;

import com.example.contact_service.entity.Contact;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "contact-service")
public interface ContactService {
    @GetMapping("/contacts")
    List<Contact> readContactsFromFile(@RequestParam("file") MultipartFile file);
}