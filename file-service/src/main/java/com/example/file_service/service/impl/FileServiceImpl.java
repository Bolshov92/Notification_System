package com.example.file_service.service.impl;

import com.example.contact_service.entity.Contact;
import com.example.file_service.entity.File;
import com.example.file_service.repository.FileRepository;
import com.example.file_service.service.ContactService;
import com.example.file_service.service.FileService;
import com.example.file_service.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;


@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ContactService contactService;

    @Autowired
    private SmsService smsService;

    @Override
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    @Override
    public File getFileById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    @Override
    public File saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            logger.error("Received an empty file.");
            throw new IllegalArgumentException("File is empty.");
        }

        File dbFile = new File();
        dbFile.setFileName(file.getOriginalFilename());
        dbFile.setType(file.getContentType());
        dbFile.setData(file.getBytes());
        dbFile.setFilePath("/some/path");
        dbFile.setUploadTime(new Timestamp(System.currentTimeMillis()));

        File savedFile = fileRepository.save(dbFile);

        List<Contact> contacts = contactService.readContactsFromFile(file);
        for (Contact contact : contacts) {
            String message = "Hi, " + contact.getName() + ". This is an emergency notification.";
            smsService.sendSms(contact.getPhoneNumber(), message);
        }

        return savedFile;
    }

    @Override
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }
}