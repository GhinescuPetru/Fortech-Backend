package org.fortech.navigation.security.services;

import org.fortech.navigation.security.models.Email;
import org.fortech.navigation.security.payload.EmailPayload;
import org.fortech.navigation.security.repos.EmailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationBroadcastService {
    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private EmailRepo emailRepo;

    public Email addEmail(Email email)
    {
        return emailRepo.save(email);
    }

    public void sendEmails(EmailPayload emailPayload)
    {
        List<Email> allEmails=emailRepo.findAll();
        for(Email email:allEmails)
        {
            emailSenderService.sendEmail(email.getEmail(),emailPayload.getSubject(),emailPayload.getMessage());
        }
    }
}
