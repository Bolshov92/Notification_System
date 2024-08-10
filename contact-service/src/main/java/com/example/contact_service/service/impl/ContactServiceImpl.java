package com.example.contact_service.service.impl;

import com.example.contact_service.entity.Contact;
import com.example.contact_service.service.ContactService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class ContactServiceImpl implements ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Override
    public List<Contact> readContactsFromFile(MultipartFile file) throws IOException {
        List<Contact> contacts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            int lineNumber = 0;
            while ((line = csvReader.readNext()) != null){
                lineNumber++;
                if (line.length >= 2) {
                    String name = line[0];
                    String phoneNumber = line[1];
                    Contact contact = new Contact(name, phoneNumber);
                    contacts.add(contact);
                    kafkaTemplate.send("contact_topic", contact);
                    logger.info("Read contact: {}", contact);
                } else {
                    logger.warn("Skipping line {} as it does not have enough elements", lineNumber);
                }
            }
        } catch (IOException e) {
            throw new IOException("Failed to read contacts from file", e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return contacts;
    }

}