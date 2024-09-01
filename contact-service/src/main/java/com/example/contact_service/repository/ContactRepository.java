package com.example.contact_service.repository;

import com.example.contact_service.entity.Contact;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
