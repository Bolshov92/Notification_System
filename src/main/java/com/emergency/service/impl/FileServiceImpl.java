package com.emergency.service.impl;

import com.emergency.entity.Contact;
import com.emergency.entity.File;
import com.emergency.repository.FileRepository;
import com.emergency.service.ContactService;
import com.emergency.service.FileService;
import com.emergency.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private FileRepository fileRepository;
    private ContactService contactService;
    private SmsService smsService;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository, ContactService contactService, SmsService smsService) {
        this.fileRepository = fileRepository;
        this.contactService = contactService;
        this.smsService = smsService;
    }


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
        File dbFile = new File();
        dbFile.setFileName(file.getOriginalFilename());
        dbFile.setType(file.getContentType());
        dbFile.setData(file.getBytes());

        logger.info("Saving file to database: {}", dbFile.getFileName());
        fileRepository.save(dbFile);
        logger.info("File saved successfully");

        try {
            List<Contact> contacts = contactService.readContactsFromFile(file);
            logger.info("Read {} contacts from file", contacts.size());

            for (Contact contact : contacts) {
                String message = "Hi, " + contact.getName() + ". This is an emergency notification.";
                logger.info("Sending SMS to {}: {}", contact.getPhoneNumber(), message);
                smsService.sendSms(contact.getPhoneNumber(), message);
            }
        } catch (Exception e) {
            logger.error("Error reading contacts from file or sending SMS", e);
            throw new RuntimeException("Failed to upload and process the file", e);
        }

        return dbFile;
    }

    @Override
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }
}
