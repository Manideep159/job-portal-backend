package com.example.Status.of.application.repository;

import com.example.Status.of.application.entity.ApplicationStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationStatusHistoryRepository extends JpaRepository<ApplicationStatusHistory, Long> {

    List<ApplicationStatusHistory>
    findByApplicationIdOrderByChangedAtAsc(Long applicationId);
}
