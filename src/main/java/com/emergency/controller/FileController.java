package com.emergency.controller;

import com.emergency.entity.File;
import com.emergency.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping
    public List<File> getAllFiles() {
        return fileService.getAllFiles();
    }

    @GetMapping("/{id}")
    public File getFileById(@PathVariable Long id) {
        return fileService.getFileById(id);
    }

    @PostMapping("/upload")
    public File uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            return fileService.saveFile(file);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload and process the file", e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
    }
}
