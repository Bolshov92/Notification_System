CREATE TABLE event
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_name    VARCHAR(255) NOT NULL UNIQUE,
    event_message TEXT         NOT NULL
);