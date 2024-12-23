package com.example.file_service.repository;

import com.example.file_service.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileName(String fileName);
}
