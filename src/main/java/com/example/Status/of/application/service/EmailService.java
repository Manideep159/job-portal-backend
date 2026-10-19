package com.example.Status.of.application.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from}")
    private String from;

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
            Resend resend = new Resend(apiKey);

            CreateEmailOptions params =
                    CreateEmailOptions.builder()
                            .from(from)
                            .to(to)
                            .subject("Job Application Status Update")
                            .text(
                                    "Your application for " + jobTitle
                                            + " is now " + status + "."
                            )
                            .build();

            CreateEmailResponse response =
                    resend.emails().send(params);

            System.out.println(
                    "EMAIL SUBMITTED TO RESEND: " + response.getId()
            );

        } catch (ResendException e) {
            System.err.println(
                    "RESEND EMAIL FAILED: " + e.getMessage()
            );
        }
    }
//            SimpleMailMessage message =
//                    new SimpleMailMessage();
//
//            message.setTo(to);
//
//            message.setSubject(
//                    "Job Application Status Update"
//            );
//
//            message.setText(
//                    "Your application for "
//                            + jobTitle
//                            + " is now "
//                            + status
//            );
//
//            if (mailSender instanceof JavaMailSenderImpl sender) {
//                System.out.println("SMTP HOST: " + sender.getHost());
//                System.out.println("SMTP PORT: " + sender.getPort());
//                System.out.println("SMTP USERNAME CONFIGURED: "
//                        + (sender.getUsername() != null
//                        && !sender.getUsername().isBlank()));
//            }
//
//            mailSender.send(message);
//            System.out.println("EMAIL SENT SUCCESSFULLY");
//
//        } catch (Exception e) {
//            System.out.println("EMAIL SENT FAILED:" + e.getMessage());
//            e.printStackTrace();
//
//        }
//    }
}
