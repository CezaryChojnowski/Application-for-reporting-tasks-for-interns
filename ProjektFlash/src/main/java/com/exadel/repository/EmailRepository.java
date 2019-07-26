package com.exadel.repository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository {
    public void sendEmail(SimpleMailMessage email);
}
