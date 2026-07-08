package com.example.Status.of.application.controller;

import com.example.Status.of.application.dto.ApplicationResponseDTO;
import com.example.Status.of.application.entity.Application;
import com.example.Status.of.application.entity.ApplicationStatus;
import com.example.Status.of.application.entity.Job;
import com.example.Status.of.application.entity.User;
import com.example.Status.of.application.mapper.ApplicationMapper;
import com.example.Status.of.application.repository.ApplicationRepository;
import com.example.Status.of.application.repository.JobRepository;
import com.example.Status.of.application.repository.UserRepository;
import com.example.Status.of.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    // 🔐 Admin: View all applications
    @GetMapping("/applications")
    public List<Application> getAll() {
        return applicationService.getAllApplications();
    }

    // 🔐 Admin: Update status
//    @PutMapping("/admin/applications/{id}/status")
//    public Application updateStatus(@PathVariable Long id,
//                                    @RequestParam ApplicationStatus status) {
//        Application app = applicationRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("NOT FOUND"));
//
//        app.setStatus(status);
//        return applicationService.updateStatus(id, status);
//    }
    @PutMapping("/applications/{id}/status")
    public ApplicationResponseDTO updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {

//        System.out.println("ADMIN UPDATE STATUS API HIT");
//        System.out.println("APPLICATION ID: " + id);
//        System.out.println("STATUS: " + status);

        Application app = applicationService.updateStatus(id, status);
        Job job = jobRepository.findById(app.getJobId())
                .orElseThrow();

        User user = userRepository.findByMobileNumber(app.getMobileNumber())
                .orElseThrow();
        return ApplicationMapper.toDTO(app, user, job);
    }

    @GetMapping("/applications/{id}/resume")
    public ResponseEntity<Resource> downloadResume(
            @PathVariable Long id
    ) throws IOException {

        Application app =
                applicationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));

        Path path =
                Paths.get(app.getResumePath());

        Path filePath = Paths.get("uploads", app.getResumeFileName());
        System.out.println("FILE EXISTS = " + Files.exists(filePath));
        System.out.println("PATH = " + filePath.toAbsolutePath());


        Resource resource =
                new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\""
                                + app.getResumeFileName()
                                + "\""
                )
                .body(resource);
    }

    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        return applicationService.getApplicationStats();
    }

    @GetMapping("/applications/my")
    public List<ApplicationResponseDTO> getMyApps(Authentication auth) {

        String mobile = auth.getName();

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
