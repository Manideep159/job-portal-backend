package com.example.Status.of.application.mapper;

import com.example.Status.of.application.dto.ApplicationResponseDTO;
import com.example.Status.of.application.entity.Application;
import com.example.Status.of.application.entity.Job;
import com.example.Status.of.application.entity.User;

public class ApplicationMapper {

    public static ApplicationResponseDTO toDTO(
            Application app,
            User user,
            Job job
    ) {

        String title = job != null ? job.getTitle() : "";
        String company = job != null ? job.getCompany() : "";
        String location = job != null ? job.getLocation() : "";

        String resumePath =
                user != null ? user.getResumePath() : null;

        String appliedDate =
                app.getAppliedDate() != null
                        ? app.getAppliedDate().toString()
                        : "";

        String status =
                app.getStatus() != null
                        ? app.getStatus().name()
                        : "";

        return new ApplicationResponseDTO(
                app.getId(),
                app.getJobId(),
                title,
                company,
                location,
                app.getMobileNumber(),
                resumePath,
                status,
                appliedDate
        );
    }
}