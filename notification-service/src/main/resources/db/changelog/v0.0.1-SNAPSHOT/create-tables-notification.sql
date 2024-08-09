CREATE TABLE notification
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id          BIGINT      NOT NULL,
    contact_id        BIGINT      NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    status            VARCHAR(50) NOT NULL,
    sent_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);