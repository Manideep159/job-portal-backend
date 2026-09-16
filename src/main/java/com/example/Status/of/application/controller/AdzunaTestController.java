package com.example.Status.of.application.controller;

import com.example.Status.of.application.dto.AdzunaResponseDTO;
import com.example.Status.of.application.service.AdzunaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class AdzunaTestController {

    private final AdzunaService adzunaService;

    public AdzunaTestController(AdzunaService adzunaService) {
        this.adzunaService = adzunaService;
    }

    @GetMapping("/adzuna")
    public AdzunaResponseDTO testAdzuna(
            @RequestParam(defaultValue = "Java Developer")
            String keyword) {

        return adzunaService.fetchJobs(keyword);
    }
}
