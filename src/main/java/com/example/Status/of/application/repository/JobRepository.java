package com.example.Status.of.application.repository;

import com.example.Status.of.application.entity.Job;
import com.example.Status.of.application.entity.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByType(JobType jobType);

    List<Job> findByLocationContainingIgnoreCase(String location);

    Job findByJobId(Long id);

    Page<Job> findAll(Pageable pageable);

    List<Job> findByLocationAndType(String location, JobType type);

    List<Job> findByLocation(String location);
}
