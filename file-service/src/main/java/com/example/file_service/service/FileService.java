package com.example.file_service.service;

import com.example.file_service.entity.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


public interface FileService {
    List<File> getAllFiles();
    File getFileById(Long id);
    ResponseEntity<String> saveFile(MultipartFile file)throws IOException;
    void deleteFile(Long id);
}
