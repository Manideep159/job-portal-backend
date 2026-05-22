package com.example.Status.of.application.repository;

import com.example.Status.of.application.entity.Application;
import com.example.Status.of.application.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByUserMobile(String userMobile);

    Application findByJobId(Long jobId);

//    boolean findByJobIdAndUserMobile(Long jobId, String userMobile);

    boolean existsByJobIdAndUserMobile(Long jobId, String userMobile);

    long countByUserMobile(String userMobile);


    long countByStatus(ApplicationStatus status);
}
