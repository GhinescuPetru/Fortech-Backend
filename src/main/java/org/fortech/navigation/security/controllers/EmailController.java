package org.fortech.navigation.security.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.fortech.navigation.security.models.EmailMessageUtil;
import org.fortech.navigation.security.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/emails") //Suppress warnings for mapping annotation
public class EmailController {
    @Autowired
    private final EmailSenderService emailSenderService;

    public EmailController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping
    public ResponseEntity sendEmail(@RequestBody EmailMessageUtil emailMessage){
        this.emailSenderService.sendEmail(emailMessage.getTo(),emailMessage.getSubject(),emailMessage.getMessage());
        return ResponseEntity.ok("Success");
    }
}
