package com.example.Status.of.application.security;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class JobAggregatorService {

    @Scheduled(cron = "0 0 */6 * * *")
    public void fetchJobs() {
        // fetch jobs every 6 hours
    }
}
