package com.emergency.service.impl;

import com.emergency.entity.Contact;
import com.emergency.service.ContactService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Override
    public List<Contact> readContactsFromFile(MultipartFile file) throws IOException {
        List<Contact> contacts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                if (line.length >= 2) {
                    Contact contact = new Contact(line[0], line[1]);
                    contacts.add(contact);
                } else {
                    throw new IOException("Invalid CSV format: Each line must have at least 2 fields");
                }
            }
        } catch (CsvValidationException e) {
            throw new IOException("Error validating CSV file", e);
        }

        return contacts;
    }
}