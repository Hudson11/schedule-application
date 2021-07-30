package io.github.Hudson11.scheduleapi.repository;

import io.github.Hudson11.scheduleapi.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
    Optional<Contact> findByName(String name);
}
