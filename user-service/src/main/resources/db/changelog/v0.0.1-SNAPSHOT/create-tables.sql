CREATE TABLE IF NOT EXISTS role
(
    role_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255),
    email        VARCHAR(255),
    phone_number VARCHAR(255),
    UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS user_info
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    password  VARCHAR(255),
    user_name VARCHAR(255) UNIQUE,
    role_id   BIGINT,
    user_id   BIGINT,
    FOREIGN KEY (role_id) REFERENCES role (role_id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);