package org.fortech.navigation.security.services;

import lombok.extern.slf4j.Slf4j;
import org.fortech.navigation.security.models.Car;
import org.fortech.navigation.security.models.User;
import org.fortech.navigation.security.repos.CarRepo;
import org.fortech.navigation.security.repos.UserRepo;
import org.fortech.navigation.security.templates.EmailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class AlertService {
    @Autowired
    private CarRepo carRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailSenderService emailSenderService;

    private long calculateDays(Instant chosenDate) {
        Instant currentDate = Instant.now();
        long daysUntilService = ChronoUnit.DAYS.between(currentDate, chosenDate);
        return daysUntilService;
    }

    private void checkITPExpiration(Car car,User user)
    {
        String subject = EmailTemplate.ITP_SUBJECT;
        String message = EmailTemplate.ITP_MESSAGE;
        String tempmessage = message;
        tempmessage = tempmessage.replace("{brand}", car.getBrand())
                                 .replace("{model}", car.getModel())
                                 .replace("{plate_number}", car.getPlateNumber());
        if (car.getItpDate() != null) {
            long daysUntilService = calculateDays(car.getItpDate());
            long absdays=Math.abs(daysUntilService);
            tempmessage = tempmessage.replace("{days}", String.valueOf(absdays));
            if (daysUntilService <= 30 && daysUntilService > 0) {
                tempmessage = tempmessage.replace("{prep}", "in");
                emailSenderService.sendEmail(user.getEmail(), subject, tempmessage);

            } else if (daysUntilService < 0) {
                tempmessage = tempmessage.replace("{prep}", "de");
                emailSenderService.sendEmail(user.getEmail(), subject, tempmessage);
            } else if(daysUntilService==0)
            {
                String messageexpirare=EmailTemplate.ITP_MESSAGE_2;
                messageexpirare=messageexpirare.replace("{brand}", car.getBrand())
                        .replace("{model}", car.getModel())
                        .replace("{plate_number}", car.getPlateNumber());
                emailSenderService.sendEmail(user.getEmail(), subject, messageexpirare);
            }
        }
    }

    private void checkServiceExpiration(Car car, User user) {
        String subject = EmailTemplate.SERVICE_SUBJECT;
        String message = EmailTemplate.SERVICE_MESSAGE;
        String tempmessage = message;
        tempmessage = tempmessage.replace("{brand}", car.getBrand())
                .replace("{model}", car.getModel())
                .replace("{plate_number}", car.getPlateNumber());

        if (car.getServiceDate() != null) {
            long daysUntilService = calculateDays(car.getServiceDate());
            long absdays = Math.abs(daysUntilService);
            tempmessage = tempmessage.replace("{days}", String.valueOf(absdays));
             if (daysUntilService <= 30 && daysUntilService > 0) {
                tempmessage = tempmessage.replace("{prep}", "în");
                emailSenderService.sendEmail(user.getEmail(), subject, tempmessage);
            } else if (daysUntilService < 0) {
                tempmessage = tempmessage.replace("{prep}", "de");
                emailSenderService.sendEmail(user.getEmail(), subject, tempmessage);
            }
            else if(daysUntilService==0)
            {
                String messageexpirare=EmailTemplate.SERVICE_MESSAGE_2;
                messageexpirare=messageexpirare.replace("{brand}", car.getBrand())
                        .replace("{model}", car.getModel())
                        .replace("{plate_number}", car.getPlateNumber());
                emailSenderService.sendEmail(user.getEmail(), subject, messageexpirare);
            }
        }
    }

    private void checkOilChangeExpiration(Car car, User user) {
        String subject = EmailTemplate.OILCHANGE_SUBJECT;
        String message = EmailTemplate.OILCHANGE_MESSAGE;
        String tempmessage = message;
         tempmessage = tempmessage.replace("{brand}", car.getBrand())
                .replace("{model}", car.getModel())
                .replace("{plate_number}", car.getPlateNumber());

        if (car.getOilChange() != null) {
            long daysUntilService = calculateDays(car.getOilChange());
            long absdays = Math.abs(daysUntilService);
            tempmessage = tempmessage.replace("{days}", String.valueOf(absdays));
             if (daysUntilService <= 30 && daysUntilService > 0) {
                tempmessage = tempmessage.replace("{prep}", "în");
                emailSenderService.sendEmail(user.getEmail(), subject, tempmessage);
            } else if (daysUntilService < 0) {
                tempmessage = tempmessage.replace("{prep}", "de");
                emailSenderService.sendEmail(user.getEmail(), subject, tempmessage);
            }else if(daysUntilService==0)
             {
                 String messageexpirare=EmailTemplate.OILCHANGE_MESSAGE_2;
                 messageexpirare=messageexpirare.replace("{brand}", car.getBrand())
                         .replace("{model}", car.getModel())
                         .replace("{plate_number}", car.getPlateNumber());
                 emailSenderService.sendEmail(user.getEmail(), subject, messageexpirare);
             }
        }
    }

    private void checkVignetteExpiration(Car car, User user) {
        String subject = EmailTemplate.VIGNETTE_SUBJECT;
        String message = EmailTemplate.VIGNETTE_MESSAGE;
        String tempmessage = message;
         tempmessage = tempmessage.replace("{brand}", car.getBrand())
                .replace("{model}", car.getModel())
                .replace("{plate_number}", car.getPlateNumber());

        if (car.getVignette() != null) {
            long daysUntilService = calculateDays(car.getVignette());
            long absdays = Math.abs(daysUntilService);
            tempmessage = tempmessage.replace("{days}", String.valueOf(absdays));
            if (daysUntilService <= 30 && daysUntilService > 0) {
                tempmessage = tempmessage.replace("{prep}", "în");
                emailSenderService.sendEmail(user.getEmail(), subject, tempmessage);
            } else if (daysUntilService < 0) {
                tempmessage = tempmessage.replace("{prep}", "de");
                emailSenderService.sendEmail(user.getEmail(), subject, tempmessage);
            }
            else if(daysUntilService==0)
            {
                String messageexpirare=EmailTemplate.VIGNETTE_MESSAGE_2;
                messageexpirare=messageexpirare.replace("{brand}", car.getBrand())
                        .replace("{model}", car.getModel())
                        .replace("{plate_number}", car.getPlateNumber());
                emailSenderService.sendEmail(user.getEmail(), subject, messageexpirare);
            }
        }
    }

    @Scheduled(cron = "0 */2 * * * *")
    public void checkAndSendAlerts() {
        try {
            log.info("Scheduled task started");
            List<User> allUsers = userRepo.findAll();

            for (User user : allUsers) {
                List<Car> cars = carRepo.findByUser(user);
                for (Car car : cars) {
                    checkITPExpiration(car,user);
                    checkServiceExpiration(car,user);
                    checkOilChangeExpiration(car,user);
                    checkVignetteExpiration(car,user);
                }
            }
            log.info("Scheduled task finished");
        }catch( Exception e) {
            e.printStackTrace();
        }
    }
}
