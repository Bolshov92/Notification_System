package com.example.smsservice.smsRepository;

import com.example.smsservice.entity.SmsLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<SmsLog, Long> {
    SmsLog findByMessageSid(String messageSid);
}
