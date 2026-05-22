package com.example.Status.of.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationResponseDTO {
    private Long id;
    private Long jobId;
    private String status;
    private String appliedDate;
}
