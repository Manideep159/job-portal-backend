package com.example.Status.of.application.mapper;

import com.example.Status.of.application.dto.JobRequestDTO;
import com.example.Status.of.application.dto.JobResponseDTO;
import com.example.Status.of.application.entity.Job;
import com.example.Status.of.application.entity.JobType;

import java.time.LocalDate;

public class JobMapper {

    public static Job toEntity(JobRequestDTO dto) {
        Job job = new Job();
        job.setTitle(dto.getTitle());
        job.setCompany(dto.getCompany());
        job.setType(JobType.valueOf(dto.getType()));
        job.setLocation(dto.getLocation());
        job.setDescription(dto.getDescription());
        job.setApplyLink(dto.getApplyLink());
        job.setLastDate(LocalDate.parse(dto.getLastDate()));
        return job;
    }

    public static JobResponseDTO toDTO(Job job) {
        return new JobResponseDTO(
                job.getJobId(),
                job.getTitle(),
                job.getCompany(),
                job.getType().name(),
                job.getLocation()
        );
    }
}
