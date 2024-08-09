CREATE TABLE sms_logs (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          to_phone_number VARCHAR(15) NOT NULL,
                          message TEXT NOT NULL,
                          status VARCHAR(20),
                          sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);