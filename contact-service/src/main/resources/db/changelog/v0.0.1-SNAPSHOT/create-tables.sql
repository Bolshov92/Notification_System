CREATE TABLE contact
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_id       BIGINT       NOT NULL,
    name         VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15)  NOT NULL
);