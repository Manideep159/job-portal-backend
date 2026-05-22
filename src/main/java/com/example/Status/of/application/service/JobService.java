package com.example.Status.of.application.service;

import com.example.Status.of.application.entity.Job;
import com.example.Status.of.application.entity.JobType;
import com.example.Status.of.application.entity.SavedJob;
import com.example.Status.of.application.repository.ApplicationRepository;
import com.example.Status.of.application.repository.JobRepository;
import com.example.Status.of.application.repository.SavedJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private SavedJobRepository savedJobRepository;

    public Job createJob(Job job) {
        return jobRepository.save(job);

    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getJobsByType(JobType jobType) {
        return jobRepository.findByType(jobType);
    }

    public List<Job> searchByLocation(String location) {
        return jobRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Job> filterJobs(String location, JobType type) {

        if (location != null && type != null) {
            return jobRepository.findByLocationAndType(location, type);
        } else if (location != null) {
            return jobRepository.findByLocation(location);
        } else if (type != null) {
            return jobRepository.findByType(type);
        } else {
            return jobRepository.findAll();
        }
    }

    public void saveJob(Long jobId, String mobile) {

        boolean alreadySaved =
//                savedJobRepository
//                        .existsByUserMobileAndJobId(
//                                mobile,
//                                jobId
//                        );
                savedJobRepository.existsByUserMobileAndId(mobile, jobId);

        if (alreadySaved) {
            return;
        }

        Job job = jobRepository
                .findById(jobId)
                .orElseThrow();

        SavedJob savedJob = new SavedJob();

        savedJob.setUserMobile(mobile);

        savedJob.setJob(job);

        savedJobRepository.save(savedJob);
    }

    public List<Job> getSavedJobs(String mobile) {

        return savedJobRepository.findByUserMobile(mobile)
                .stream()
                .map(SavedJob::getJob)
                .toList();
    }

    public Job getJobById(Long id) {
        return jobRepository.findByJobId(id);
    }

    public Page<Job> getJobs(int page, int size) {
        return jobRepository.findAll(PageRequest.of(page, size));
    }
}
