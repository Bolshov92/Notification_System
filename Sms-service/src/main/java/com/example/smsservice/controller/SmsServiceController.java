package com.example.smsservice.controller;

import com.example.smsservice.dto.SmsRequestDto;
import com.example.smsservice.entity.SmsLog;
import com.example.smsservice.smsRepository.SmsRepository;
import com.example.smsservice.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/sms")
public class SmsServiceController {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceController.class);

    private final SmsService smsService;
    private final SmsRepository smsRepository;

    @Autowired
    public SmsServiceController(SmsService smsService, SmsRepository smsRepository) {
        this.smsService = smsService;
        this.smsRepository = smsRepository;
    }

    @PostMapping("/status")
    public ResponseEntity<String> handleSmsStatus(@RequestParam Map<String, String> requestParams) {
        String messageSid = requestParams.get("MessageSid");
        String messageStatus = requestParams.get("MessageStatus");

        logger.info("Received status update for MessageSid {}: {}", messageSid, messageStatus);

        SmsLog smsLog = smsRepository.findByMessageSid(messageSid);
        if (smsLog != null) {
            smsLog.setStatus(messageStatus);
            smsRepository.save(smsLog);

            smsService.sendStatusUpdate(messageSid, messageStatus);
        } else {
            logger.error("SmsLog not found for MessageSid {}", messageSid);
        }

        return new ResponseEntity<>("Status received", HttpStatus.OK);
    }
}
