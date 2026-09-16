package com.example.Status.of.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdzunaJobDTO {

    private String id;

    private String title;

    private String description;

    private AdzunaCompanyDTO Company;

    private AdzunaLocationDTO location;

    @JsonProperty("redirect_url")
    private String redirectUrl;

    private String created;
}
