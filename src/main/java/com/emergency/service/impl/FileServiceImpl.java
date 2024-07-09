package com.emergency.service.impl;

import com.emergency.entity.Contact;
import com.emergency.entity.File;
import com.emergency.repository.FileRepository;
import com.emergency.service.ContactService;
import com.emergency.service.FileService;
import com.emergency.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private FileRepository fileRepository;
    private ContactService contactService;
    private SmsService smsService;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
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
        fileRepository.save(dbFile);
        List<Contact> contacts = contactService.readContactsFromFile(file);
        for (Contact contact : contacts) {
            String message = "Hi, " + contact.getName() + "This is emergency Notification ";
            smsService.sendSms(contact.getPhoneNumber(), message);
        }
        return dbFile;
    }

    @Override
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }
}
