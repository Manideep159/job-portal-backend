package com.example.Status.of.application.schedular;

import com.example.Status.of.application.service.JobAutomationService;
import org.springframework.boot.CommandLineRunner;

public class JobFetchRunner implements CommandLineRunner {

    private final JobAutomationService jobAutomationService;

    public JobFetchRunner(
            JobAutomationService jobAutomationService) {

        this.jobAutomationService =
                jobAutomationService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 &&
                "fetch-jobs".equals(args[0])) {

            System.out.println(
                    "Starting automatic job fetch..."
            );

            jobAutomationService.fetchAndSaveJobs();

            System.out.println(
                    "Automatic job fetch completed."
            );
        }
    }
}
