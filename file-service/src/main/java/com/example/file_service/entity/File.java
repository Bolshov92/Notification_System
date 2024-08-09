package com.example.file_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String type;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "upload_time")
    private Timestamp uploadTime;

    @Lob
    @Column(name = "data")
    private byte[] data;


}
