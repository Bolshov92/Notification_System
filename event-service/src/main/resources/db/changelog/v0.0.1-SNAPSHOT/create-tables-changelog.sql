CREATE TABLE Event
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_name        VARCHAR(255) NOT NULL,
    event_date        DATE         NOT NULL,
    notification_text TEXT         NOT NULL
);