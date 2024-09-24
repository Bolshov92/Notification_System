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