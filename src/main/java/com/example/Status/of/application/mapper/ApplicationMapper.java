package com.example.Status.of.application.mapper;

import com.example.Status.of.application.dto.ApplicationResponseDTO;
import com.example.Status.of.application.entity.Application;
import com.example.Status.of.application.entity.Job;
import com.example.Status.of.application.entity.User;

public class ApplicationMapper {

    public static ApplicationResponseDTO toDTO(Application app, User user, Job job) {
        return new ApplicationResponseDTO(
//                app.getId(),
//                app.getJobId(),
//                app.getStatus().name(),
//                app.getAppliedDate().toString()
                app.getId(),
                app.getJobId(),
                app.getJob().getTitle(),
                app.getJob().getCompany(),
                app.getJob().getLocation(),
                app.getMobileNumber(),
                app.getUser().getResumePath(),
                app.getStatus().name(),
                app.getAppliedDate().toString()
        );
    }
}