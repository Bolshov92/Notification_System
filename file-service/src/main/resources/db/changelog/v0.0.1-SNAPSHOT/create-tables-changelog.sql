CREATE TABLE files
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name   VARCHAR(255),
    file_type   VARCHAR(255),
    file_path   VARCHAR(255),
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data        LONGBLOB
);
