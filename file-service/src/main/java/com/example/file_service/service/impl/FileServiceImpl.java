package com.example.file_service.service.impl;

import com.example.file_service.entity.File;
import com.example.file_service.repository.FileRepository;
import com.example.file_service.service.FileProducer;
import com.example.file_service.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

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
    public ResponseEntity<String> saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        String fileName = file.getOriginalFilename();
        Optional<File> existingFile = fileRepository.findByFileName(fileName);

        if (existingFile.isPresent()) {
            return ResponseEntity.badRequest().body("File with this name already exists.");
        }

        File dbFile = new File();
        dbFile.setFileName(fileName);
        dbFile.setType(file.getContentType());
        dbFile.setData(file.getBytes());
        dbFile.setFilePath("/some/path");
        dbFile.setUploadTime(new Timestamp(System.currentTimeMillis()));

        File savedFile = fileRepository.save(dbFile);
        Long fileId = savedFile.getId();
        String filePath = savedFile.getFilePath();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String name = parts[0].trim();
                    String phoneNumber = parts[1].trim();
                    fileProducer.sendMessage(fileId, fileName, name, phoneNumber);
                } else {
                    return ResponseEntity.status(400).body("Invalid line format in the file.");
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error reading the file.");
        }

        return ResponseEntity.ok("File uploaded successfully.");
    }

    @Override
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }
}
