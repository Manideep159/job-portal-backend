package com.example.Status.of.application.service;

import com.example.Status.of.application.entity.Job;
import com.example.Status.of.application.entity.JobType;
import com.example.Status.of.application.repository.JobRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GovernmentJobService {

    private final JobRepository jobRepository;

    private static final String EMPLOYMENT_NEWS_URL =
            "https://employmentnews.gov.in/newemp/AllJobs.aspx?k=All";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public GovernmentJobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public int fetchGovernmentJobs() throws IOException {

        Document document = Jsoup.connect(EMPLOYMENT_NEWS_URL)
                .userAgent("Mozilla/5.0")
                .timeout(15000)
                .get();

        Elements rows = document.select("table tr");

        int savedCount = 0;

        for (Element row : rows) {

            Elements columns = row.select("td");

            if (columns.size() < 5) {
                continue;
            }

            String issuedDate = columns.get(0).text().trim();
            String organization = columns.get(1).text().trim();
            String post = columns.get(2).text().trim();
            String method = columns.get(3).text().trim();
            String lastDate = columns.get(4).text().trim();

            // Skip table header row
            if (issuedDate.toUpperCase().contains("ISSUED DATE")
                    || lastDate.toUpperCase().contains("LAST DATE")) {
                continue;
            }

            if (organization.isBlank() || post.isBlank()) {
                continue;
            }

            String externalJobId = createExternalJobId(
                    organization,
                    post,
                    lastDate
            );

            if (jobRepository.findByExternalJobId(externalJobId).isPresent()) {
                continue;
            }

            Job job = new Job();

            job.setExternalJobId(externalJobId);
            job.setTitle(post);
            job.setCompany(organization);
            job.setType(JobType.GOVT);
            job.setLocation("India");

            job.setDescription(
                    "Government job opportunity. " +
                            "Method of appointment: " + method
            );

            job.setApplyLink(EMPLOYMENT_NEWS_URL);

            if (!lastDate.isBlank()) {
                try {
                    job.setLastDate(
                            LocalDate.parse(lastDate, DATE_FORMATTER)
                    );
                } catch (Exception e) {
                    System.out.println("Unable to parse last date: " + lastDate);
                }
            }

            if (!issuedDate.isBlank()) {
                try {
                    LocalDate issued = LocalDate.parse(
                            issuedDate,
                            DateTimeFormatter.ofPattern("MM/dd/yyyy")
                    );

                    job.setCreatedAt(issued.atStartOfDay());

                } catch (Exception e) {
                    System.out.println("Unable to parse issued date: " + issuedDate);
                    job.setCreatedAt(LocalDateTime.now());
                }
            } else {
                job.setCreatedAt(LocalDateTime.now());
            }

            jobRepository.save(job);

            savedCount++;
        }

        return savedCount;
    }

    private String createExternalJobId(
            String organization,
            String post,
            String lastDate) {

        return "employment-news-"
                + organization
                + "-"
                + post
                + "-"
                + lastDate;
    }
}