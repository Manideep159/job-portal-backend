package com.example.Status.of.application.repository;

import com.example.Status.of.application.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedJobRepository
        extends JpaRepository<SavedJob, Long> {

    List<SavedJob> findByUserMobile(String userMobile);

    boolean existsByUserMobileAndId(String userMobile, Long jobId);

    long countByUserMobile(String mobile);
}