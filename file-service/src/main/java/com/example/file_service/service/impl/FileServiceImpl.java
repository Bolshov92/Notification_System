package com.example.file_service.service.impl;

import com.example.file_service.entity.File;
import com.example.file_service.repository.FileRepository;
import com.example.file_service.service.FileProducer;
import com.example.file_service.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileProducer fileProducer;

    @Autowired
    private FileRepository fileRepository;

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
        Long fileId = savedFile.getId();
        String fileName = savedFile.getFileName();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String name = parts[0].trim();
                    String phoneNumber = parts[1].trim();
                    fileProducer.sendMessage(fileId, fileName, name, phoneNumber);
                } else {
                    logger.error("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading the file: " + e.getMessage());
        }

        return savedFile;
    }

    @Override
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }
}