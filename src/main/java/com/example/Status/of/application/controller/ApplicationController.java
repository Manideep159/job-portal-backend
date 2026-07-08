package com.example.Status.of.application.controller;

import com.example.Status.of.application.dto.ApplicationResponseDTO;
import com.example.Status.of.application.entity.Application;
import com.example.Status.of.application.entity.Job;
import com.example.Status.of.application.entity.User;
import com.example.Status.of.application.mapper.ApplicationMapper;
import com.example.Status.of.application.repository.JobRepository;
import com.example.Status.of.application.repository.UserRepository;
import com.example.Status.of.application.service.ApplicationService;
import com.example.Status.of.application.service.JobService;
import com.example.Status.of.application.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {


    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JobService jobService;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;


    // 🔐 Apply Job
//    @PostMapping("/{jobId}")
//    public Application apply(@PathVariable Long jobId,
//                             @RequestHeader("Authorization") String token) {
//
//        String mobile = jwtUtil.extractUsername(token.substring(7));
//        return applicationService.applyJob(jobId, mobile);
//    }
    @PostMapping("/{jobId}")
    public ResponseEntity<?> apply(@PathVariable Long jobId, Authentication authentication) throws Exception {
//        System.out.println("==== DEBUG START ====");
//        System.out.println("TOKEN RECEIVED: " + authentication);
//
//        String actualToken = authentication.substring(7);
//        System.out.println("ACTUAL TOKEN: " + actualToken);
//
//        String mobile = jwtUtil.extractUsername(actualToken);
//        System.out.println("EXTRACTED MOBILE: " + mobile);
//
//        Application app = applicationService.applyJob(jobId, mobile);
//        System.out.println("APPLICATION CREATED: " + app);
//
//        System.out.println("==== DEBUG END ====");
//        String mobile = authentication.getName();

//        applicationService.applyJob(
//                jobId
//
//        );

        String mobileNumber =
                authentication.getName();
        applicationService.applyJob(jobId, mobileNumber);
        return ResponseEntity.ok("Applied successfully");
    }

    // 🔐 Get My Applications

    @GetMapping("/job/{id}")
    public Job getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    @GetMapping("/applications/count")
    public long getCount(Authentication auth) {
        return applicationService.countByUser(auth.getName());
    }

    @GetMapping("/{id}")
    public Application getById(@PathVariable Long id) {
        return applicationService.getById(id);
    }

    //  Resume Upload with the only registered User
//    @PostMapping("/{applicationId}/resume")
//    public String uploadResume(
//            @PathVariable Long applicationId,
//            @RequestParam("file") MultipartFile file,
//            Authentication authentication) {
//
//        String mobile = authentication.getName();
//
//        return applicationService.uploadResume(applicationId, mobile, file);
//    }

    //    @GetMapping
//    public List<Application> myApplications(@RequestHeader("Authorization") String token) {
//
//        String mobile = jwtUtil.extractUsername(token.substring(7));
//        return applicationService.getUserApplications(mobile);
//    }
    @GetMapping("/my")
    public List<ApplicationResponseDTO> getMyApps(@RequestHeader("Authorization") String token) {

        String mobile = jwtUtil.extractUsername(token.substring(7));

        return applicationService.getByUser(mobile)
                .stream()
                .map(app -> {

                    Job job = jobRepository.findById(app.getJobId())
                            .orElseThrow();

                    User user = userRepository.findByMobileNumber(app.getMobileNumber())
                            .orElseThrow();

                    return ApplicationMapper.toDTO(app, user, job);

                })
                .toList();
    }
}
