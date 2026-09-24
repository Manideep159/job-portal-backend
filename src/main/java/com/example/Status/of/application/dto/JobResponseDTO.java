package com.example.Status.of.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class JobResponseDTO {
    private Long id;
    private String title;
    private String company;
    private String type;
    private String location;
    private LocalDateTime createdAt;
}
