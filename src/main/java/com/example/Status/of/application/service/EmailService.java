package com.example.Status.of.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendApplicationStatusEmail(
            String to,
            String status,
            String jobTitle
    ) {
        System.out.println("EMAIL METHOD CALLED");
        System.out.println("SENDING EMAIL TO" + to);
        System.out.println("status:" + status);
        System.out.println("Job title:" + jobTitle);

        try {
            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(to);

            message.setSubject(
                    "Job Application Status Update"
            );

            message.setText(
                    "Your application for "
                            + jobTitle
                            + " is now "
                            + status
            );

            mailSender.send(message);
            System.out.println("EMAIL SENT SUCCESSFULLY");

        } catch (Exception e) {
            System.out.println("EMAIL SENT FAILED:" + e.getMessage());
            e.printStackTrace();

        }
    }
}
