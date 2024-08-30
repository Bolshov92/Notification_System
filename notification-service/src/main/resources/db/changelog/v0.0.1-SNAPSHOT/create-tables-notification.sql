CREATE TABLE notifications
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id   BIGINT      NOT NULL,
    contact_id BIGINT      NOT NULL,
    message    TEXT        NOT NULL,
    status     VARCHAR(50) NOT NULL,
    sent_at    TIMESTAMP   NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events(id),
    FOREIGN KEY (contact_id) REFERENCES contacts(id)
);
