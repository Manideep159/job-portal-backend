package com.example.Status.of.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JobResponse(
        String id,
        String title,
        String description,
        Company company,
        Location location,
        @JsonProperty("redirect_url") String redirectUrl
) {
}

