CREATE TABLE notifications
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id     BIGINT NOT NULL,
    contact_id   BIGINT NOT NULL,
    contact_name VARCHAR(255),
    phone_number VARCHAR(15),
    message      TEXT,
    status       VARCHAR(50),
    sent_at      TIMESTAMP
);
