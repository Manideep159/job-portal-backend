package com.example.Status.of.application.service;

import com.example.Status.of.application.entity.Application;
import com.example.Status.of.application.entity.ApplicationStatus;
import com.example.Status.of.application.entity.Job;
import com.example.Status.of.application.entity.User;
import com.example.Status.of.application.repository.ApplicationRepository;
import com.example.Status.of.application.repository.JobRepository;
import com.example.Status.of.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ApplicationService {


    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    //    public Application applyJob(Long jobId, String userMobile) {
//        boolean alreadyApplied = applicationRepository
//                .existsByJobIdAndUserMobile(jobId, userMobile);
//        if (alreadyApplied) {
//            throw new RuntimeException("You already applied for this job");
//        }
    public void applyJob(
            String userMobile,
            Long jobId,
            MultipartFile file
    ) throws Exception {

        Job job = jobRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        boolean alreadyApplied =
                applicationRepository
                        .existsByJobIdAndUserMobile(
                                jobId,
                                userMobile
                        );

        if (alreadyApplied) {
            throw new RuntimeException(
                    "Already applied"
            );
        }

        String fileName =
                System.currentTimeMillis()
                        + "_"
                        + file.getOriginalFilename();

        Path uploadPath =
                Paths.get("uploads");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath =
                uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath
        );

        Application application =
                new Application();

        application.setUserMobile(userMobile);

        application.setJob(job);

        application.setStatus(ApplicationStatus.valueOf("APPLIED"));

        application.setResumeFileName(fileName);

        application.setResumePath(
                filePath.toString()
        );

        applicationRepository.save(application);

//        Application app = new Application();
//        app.setJobId(jobId);
//        app.setUserMobile(userMobile);
//        app.setStatus(ApplicationStatus.valueOf("APPLIED"));
//        app.setAppliedDate(LocalDate.now());
        System.out.println("APPLY API CALLED");

//        applicationRepository.save(application);
    }

    public Map<String, Long> getApplicationStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("TOTAL", applicationRepository.count());
        stats.put("APPLIED", applicationRepository.countByStatus(ApplicationStatus.APPLIED));
        stats.put("REVIEWED", applicationRepository.countByStatus(ApplicationStatus.REVIEWED));
        stats.put("SHORTLISTED", applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED));
        stats.put("SELECTED", applicationRepository.countByStatus(ApplicationStatus.SELECTED));
        stats.put("REJECTED", applicationRepository.countByStatus(ApplicationStatus.REJECTED));

        return stats;
    }

    public List<Application> getUserApplications(String mobile) {
        return applicationRepository.findByUserMobile(mobile);
    }


    public Application updateStatus(Long id, ApplicationStatus status) {

        System.out.println("UPDATE STATUS METHOD CALLED");

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(status);

        Application savedApplication = applicationRepository.save(application);

        System.out.println("APPLICATION STATUS UPDATED");

        User user = userRepository.findByMobileNumber(application.getUserMobile())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("USER EMAIL FOUND: " + user.getEmail());

        Job job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        System.out.println("JOB FOUND: " + job.getTitle());

        emailService.sendApplicationStatusEmail(
                user.getEmail(),
                status.name(),
                job.getTitle()
        );

        return savedApplication;
    }

    public Application getById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public List<Application> getByUser(String mobile) {
        return applicationRepository.findByUserMobile(mobile);
    }

    public long countByUser(String mobile) {
        return applicationRepository.countByUserMobile(mobile);
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

//    public String uploadResume(Long applicationId, String userMobile, MultipartFile file) {
//
//        Application application = applicationRepository.findById(applicationId)
//                .orElseThrow(() -> new RuntimeException("Application not found"));
//
//        if (!application.getUserMobile().equals(userMobile)) {
//            throw new RuntimeException("You are not allowed to upload resume for this application");
//        }
//
//        if (file.isEmpty()) {
//            throw new RuntimeException("File is empty");
//        }
//
//        String originalFileName = file.getOriginalFilename();
//
//        if (originalFileName == null ||
//                !(originalFileName.endsWith(".pdf") || originalFileName.endsWith(".doc") || originalFileName.endsWith(".docx"))) {
//            throw new RuntimeException("Only PDF, DOC, DOCX files are allowed");
//        }
//
//        try {
//            Path uploadPath = Paths.get(uploadDir);
//
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            String fileName = "resume_" + applicationId + "_" + System.currentTimeMillis() + "_" + originalFileName;
//            Path filePath = uploadPath.resolve(fileName);
//
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//            application.setResumeFileName(fileName);
//            application.setResumePath(filePath.toString());
//
//            applicationRepository.save(application);
//
//            return "Resume uploaded successfully";
//
//        } catch (IOException e) {
//            throw new RuntimeException("Resume upload failed: " + e.getMessage());
//        }
//    }

//    public String uploadResume(Long id, MultipartFile file) {
//
//        Application app = applicationRepository.findById(id)
//                .orElseThrow();
//
//        String fileName = file.getOriginalFilename();
//
//        Path path = Paths.get("uploads/" + fileName);
//
//        try {
//            Files.write(path, file.getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException("Upload failed");
//        }
//
//        app.setResumePath(path.toString());
//        applicationRepository.save(app);
//
//        return "Uploaded";
//    }
}
//@Service
//
//public class ApplicationService {
//
//    @Autowired
//    private ApplicationRepository applicationRepository;
//
//    @Autowired
//    private JobRepository jobRepository;
//
//    public Application applyJob(Long jobId, String mobile){
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new RuntimeException("Job not found"));
//
//        Application application= new Application();
//        application.setUserMobile(mobile);
//        application.setJob(job);
//        application.setStatus(ApplicationStatus.APPLIED);
//        application.setAppliedDate(LocalDate.now());
//
//
//        return applicationRepository.save(application);
//
//    }
//
//    public List<Application> getUserApplications(String mobile){
//        return applicationRepository.findByUserMobile(mobile);
//    }
//}
