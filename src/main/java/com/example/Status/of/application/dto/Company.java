package com.example.Status.of.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Company(@JsonProperty("display_name") String displayName) {
}
