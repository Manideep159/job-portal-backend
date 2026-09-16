package com.example.Status.of.application.service;

import com.example.Status.of.application.dto.AdzunaResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdzunaService {

    @Value("${adzuna.app-id}")
    private String appId;

    @Value("${adzuna.app-key}")
    private String appKey;

    private final RestTemplate restTemplate;

    public AdzunaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AdzunaResponseDTO fetchJobs(String keyword) {

        String url =
                "https://api.adzuna.com/v1/api/jobs/in/search/1"
                        + "?app_id=" + appId
                        + "&app_key=" + appKey
                        + "&results_per_page=20"
                        + "&what=" + keyword
                        + "&content-type=application/json";

        return restTemplate.getForObject(
                url,
                AdzunaResponseDTO.class
        );
    }
}