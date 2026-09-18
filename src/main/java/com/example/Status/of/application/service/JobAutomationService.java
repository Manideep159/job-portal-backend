package com.example.Status.of.application.service;

import com.example.Status.of.application.dto.AdzunaJobDTO;
import com.example.Status.of.application.dto.AdzunaResponseDTO;
import com.example.Status.of.application.entity.Job;
import com.example.Status.of.application.entity.JobType;
import com.example.Status.of.application.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JobAutomationService {

    private final AdzunaService adzunaService;
    private final JobRepository jobRepository;

    public JobAutomationService(
            AdzunaService adzunaService,
            JobRepository jobRepository) {

        this.adzunaService = adzunaService;
        this.jobRepository = jobRepository;
    }

    public void fetchAndSaveJobs() {

        List<String> keywords = List.of(
                "Java Developer",
                "Software Engineer",
                "Backend Developer"
        );
        for (String keyword : keywords) {

            try {
                System.out.println("\"Fetching jobs for: " + keyword);

                AdzunaResponseDTO response =
                        adzunaService.fetchJobs(keyword);

                if (response == null ||
                        response.getResults() == null) {

                    continue;
                }

                for (AdzunaJobDTO apiJob :
                        response.getResults()) {

                    saveIfNew(apiJob);
                }

            } catch (Exception e) {

                System.err.println(
                        "Failed to fetch jobs for "
                                + keyword
                );

                e.printStackTrace();
            }
        }
    }

    private void saveIfNew(AdzunaJobDTO apiJob) {

        String externalId =
                "adzuna-" + apiJob.getId();

        if (jobRepository.findByExternalJobId(externalId).isPresent()) {

            System.out.println("Job already exists: " + externalId);

            return;
        }

        Job job = new Job();

        job.setExternalJobId(externalId);

        job.setTitle(apiJob.getTitle());

        job.setDescription(apiJob.getDescription());

        job.setApplyLink(apiJob.getRedirectUrl());

//        job.setCompany(String.valueOf(apiJob.getCompany()));

        job.setType(JobType.valueOf("PRIVATE"));

        job.setCreatedAt(LocalDateTime.now());


        String companyName = null;
        if (apiJob.getCompany() != null
                && apiJob.getCompany().getDisplayName() != null
                && !apiJob.getCompany().getDisplayName().isBlank()) {

//            job.setCompany(apiJob.getCompany().getDisplayName());
            companyName = apiJob.getCompany().getDisplayName();

        }
        if (companyName == null && apiJob.getDescription() != null) {

            String description = apiJob.getDescription();

            Pattern pattern = Pattern.compile(
                    "(?i)job is with\\s+([A-Za-z0-9&.,'\\- ]+?)(?:,|\\s+an inclusive employer|\\s+–)"
            );

            Matcher matcher = pattern.matcher(description);

            if (matcher.find()) {
                companyName = matcher.group(1).trim();
            }
            if (companyName == null || companyName.isBlank()) {
                companyName = "Company not specified";
            }

            job.setCompany(companyName);
        }

        if (apiJob.getLocation() != null
                && apiJob.getLocation().getDisplayName() != null
                && !apiJob.getLocation().getDisplayName().isBlank()) {

            job.setLocation(apiJob.getLocation().getDisplayName());

        } else {

            job.setLocation("Location not specified");
        }

        job.setLastDate(null);

        jobRepository.save(job);

        System.out.println(
                "New job saved: "
                        + job.getTitle()
        );
    }
}

