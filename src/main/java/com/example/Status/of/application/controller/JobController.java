package com.example.Status.of.application.controller;

import com.example.Status.of.application.dto.JobRequestDTO;
import com.example.Status.of.application.dto.JobResponseDTO;
import com.example.Status.of.application.entity.Job;
import com.example.Status.of.application.entity.JobType;
import com.example.Status.of.application.mapper.JobMapper;
import com.example.Status.of.application.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    // 🔐 Protected API (JWT required)
    @PostMapping("/create")
    public Job save(@RequestBody Job job) {
//        System.out.println("JOB DATA: " + job);
        return jobService.createJob(job);
    }

    // 🔓 Public API
//    @GetMapping
//    public List<Job> getJobs() {
//        return jobService.getAllJobs();
//    }

    @GetMapping("/all")
    public List<JobResponseDTO> getAllJobs() {
        return jobService.getAllJobs()
                .stream()
                .map(JobMapper::toDTO)
                .toList();
    }

    @GetMapping("/filter")
    public List<Job> filterJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) JobType type) {

        return jobService.filterJobs(location, type);
    }

    @GetMapping("/saved")
    public List<JobResponseDTO> getSavedJobs(Authentication authentication) {

        String mobile = authentication.getName();

        return jobService.getSavedJobs(mobile)
                .stream()
                .map(JobMapper::toDTO)
                .toList();
    }

    @PostMapping("/save/{jobId}")
    public ResponseEntity<?> saveJob(
            @PathVariable Long jobId,
            Authentication authentication
    ) {

        String mobile = authentication.getName();

        jobService.saveJob(jobId, mobile);

        return ResponseEntity.ok("Job saved");
    }

    //
    @GetMapping("/page")
    public Page<JobResponseDTO> getJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return jobService.getJobs(page, size)
                .map(JobMapper::toDTO);
    }
//
//    @GetMapping("/{id}")
//    public JobResponseDTO getById(@PathVariable Long id) {
//        return JobMapper.toDTO(jobService.getJobById(id));
//    }

    @PostMapping
    public JobResponseDTO createJob(@RequestBody JobRequestDTO dto) {
        Job job = jobService.createJob(JobMapper.toEntity(dto));
        return JobMapper.toDTO(job);
    }

    @GetMapping("/type/{type}")
    public List<Job> getByType(@PathVariable JobType type) {
        return jobService.getJobsByType(type);
    }

    @GetMapping("/{id}")
    public JobResponseDTO getById(@PathVariable Long id) {
        return JobMapper.toDTO(jobService.getJobById(id));
    }

    @GetMapping("/search")
    public List<Job> search(@RequestParam String location) {
        return jobService.searchByLocation(location);
    }
}