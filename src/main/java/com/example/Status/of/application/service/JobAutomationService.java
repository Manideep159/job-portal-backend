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

        String externalId = "adzuna-" + apiJob.getId();

        // Prevent duplicate jobs
        if (jobRepository.findByExternalJobId(externalId).isPresent()) {
            System.out.println("Job already exists: " + externalId);
            return;
        }

        Job job = new Job();

        job.setExternalJobId(externalId);

        job.setTitle(apiJob.getTitle());

        job.setDescription(apiJob.getDescription());

        job.setApplyLink(apiJob.getRedirectUrl());

        job.setType(JobType.PRIVATE);

        // This is when OUR application imported the job.
        // We will use this for the NEW badge.
        job.setCreatedAt(LocalDateTime.now());


        // =====================================================
        // COMPANY
        // =====================================================

        String companyName = null;

        // 1. First try Adzuna's structured company field
        if (apiJob.getCompany() != null
                && apiJob.getCompany().getDisplayName() != null
                && !apiJob.getCompany().getDisplayName().isBlank()) {

            companyName = apiJob.getCompany().getDisplayName();
        }

        // 2. If Adzuna company is missing,
        //    try extracting it from the description
        if ((companyName == null || companyName.isBlank())
                && apiJob.getDescription() != null) {

            String description = apiJob.getDescription();

            Pattern pattern = Pattern.compile(
                    "(?i)job is with\\s+([A-Za-z0-9&.,'\\- ]+?)(?:,|\\s+an inclusive employer|\\s+–)"
            );

            Matcher matcher = pattern.matcher(description);

            if (matcher.find()) {
                companyName = matcher.group(1).trim();
            }
        }

        // 3. Final fallback
        if (companyName == null || companyName.isBlank()) {
            companyName = "Company not specified";
        }

        // IMPORTANT:
        // Set company OUTSIDE the previous if blocks
        job.setCompany(companyName);


        // =====================================================
        // LOCATION
        // =====================================================

        if (apiJob.getLocation() != null
                && apiJob.getLocation().getDisplayName() != null
                && !apiJob.getLocation().getDisplayName().isBlank()) {

            job.setLocation(apiJob.getLocation().getDisplayName());

        } else {

            job.setLocation("Location not specified");
        }


        // =====================================================
        // LAST DATE
        // =====================================================

        // Adzuna search API does not provide an application
        // deadline for this job.
        job.setLastDate(null);


        // =====================================================
        // SAVE
        // =====================================================

        jobRepository.save(job);

        System.out.println(
                "New job saved: "
                        + job.getTitle()
                        + " | Company: "
                        + job.getCompany()
                        + " | Location: "
                        + job.getLocation()
        );
    }
}

