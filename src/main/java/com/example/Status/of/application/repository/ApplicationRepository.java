package com.example.Status.of.application.repository;

import com.example.Status.of.application.entity.Application;
import com.example.Status.of.application.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    Application findByJobId(
            Long jobId
    );

    long countByMobileNumber(
            String mobileNumber
    );

    long countByStatus(
            ApplicationStatus status
    );

    boolean existsByJobIdAndMobileNumber(
            Long jobId,
            String mobileNumber
    );

    List<Application> findByMobileNumber(
            String mobileNumber
    );
}