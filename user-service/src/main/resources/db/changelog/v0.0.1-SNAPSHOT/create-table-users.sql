CREATE TABLE IF NOT EXISTS users
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255),
    email        VARCHAR(255),
    phone_number VARCHAR(255),
    UNIQUE (email)
);