package com.example.Status.of.application.controller;

import com.example.Status.of.application.service.JobAutomationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/jobs")
public class JobAutomationController {

    private final JobAutomationService jobAutomationService;

    @Value("${job.automation.secret}")
    private String automationSecret;


    public JobAutomationController(
            JobAutomationService jobAutomationService) {

        this.jobAutomationService =
                jobAutomationService;
    }

    @PostMapping("/fetch")
    public ResponseEntity<?> fetchJobs(
            @RequestHeader("X-Automation-Key") String automationKey) {

        if (!automationSecret.equals(automationKey)) {
            return ResponseEntity.status(401)
                    .body("Unauthorized");
        }

        jobAutomationService.fetchAndSaveJobs();

        return ResponseEntity.ok("Jobs fetched successfully");
    }
}
