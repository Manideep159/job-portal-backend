package com.example.Status.of.application.mapper;

import com.example.Status.of.application.dto.ApplicationResponseDTO;
import com.example.Status.of.application.entity.Application;

public class ApplicationMapper {

    public static ApplicationResponseDTO toDTO(Application app) {
        return new ApplicationResponseDTO(
                app.getId(),
                app.getJobId(),
                app.getStatus().name(),
                app.getAppliedDate().toString()
        );
    }
}