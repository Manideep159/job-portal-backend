package com.example.Status.of.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdzunaCompanyDTO {

    @JsonProperty("display_name")
    private String displayName;
}
