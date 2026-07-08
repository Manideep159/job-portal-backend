package com.example.Status.of.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponseDTO {

    private Long id;

    private Long jobId;

    private String jobTitle;

    private String company;

    private String location;

    private String userMobile;

    private String resumePath;

    private String status;

    private String appliedDate;
}