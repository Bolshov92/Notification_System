CREATE TABLE notifications
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id     BIGINT NOT NULL,
    contact_id   BIGINT NOT NULL,
    contact_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    message      TEXT NOT NULL,
    status       VARCHAR(50) NOT NULL,
    sent_at      TIMESTAMP NOT NULL
);
