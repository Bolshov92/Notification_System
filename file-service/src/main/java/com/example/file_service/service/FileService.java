package com.example.file_service.service;

import com.example.file_service.entity.File;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


public interface FileService {
    List<File> getAllFiles();
    File getFileById(Long id);
    File saveFile(MultipartFile file)throws IOException;
    void deleteFile(Long id);
}
