CREATE TABLE notifications
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id      BIGINT,
    contact_id    BIGINT,
    contact_name  VARCHAR(255),
    phone_number  VARCHAR(15),
    event_name    VARCHAR(255),
    event_message TEXT,
    status        VARCHAR(50),
    sent_at       TIMESTAMP
);