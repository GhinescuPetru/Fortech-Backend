package org.fortech.navigation.security.services;

import org.springframework.stereotype.Service;

@Service
public interface EmailSenderService {
    void sendEmail(String to,String subject,String message);
}
