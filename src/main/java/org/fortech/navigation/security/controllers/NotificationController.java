package org.fortech.navigation.security.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.fortech.navigation.security.models.Email;
import org.fortech.navigation.security.payload.EmailPayload;
import org.fortech.navigation.security.services.NotificationBroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/notifications")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    @Autowired
    private NotificationBroadcastService notificationBroadcastService;

    @PostMapping("/email")
    public ResponseEntity<String> addEmail(@RequestBody Email email) {
        try {
            notificationBroadcastService.addEmail(email);
            return ResponseEntity.ok("Email saved successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>("An error occurred while saving the email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/broadcast")
    public ResponseEntity<String> sendEmails(@RequestBody EmailPayload payload) {
        try {
            notificationBroadcastService.sendEmails(payload);
            return ResponseEntity.ok("Emails sent successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>("An error occurred while sending emails", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

