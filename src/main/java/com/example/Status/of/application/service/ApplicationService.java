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

import java.time.LocalDate;
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
            Long jobId,
            String mobileNumber
    ) throws Exception {

        Job job = jobRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        boolean alreadyApplied =
                applicationRepository
                        .existsByJobIdAndMobileNumber(
                                jobId,
                                mobileNumber
                        );

        if (alreadyApplied) {
            throw new RuntimeException(
                    "You already applied for this job"
            );
        }

        Application application =
                new Application();

        application.setJobId(jobId);

        application.setMobileNumber(
                mobileNumber
        );

        application.setStatus(
                ApplicationStatus.APPLIED
        );

        application.setAppliedDate(
                LocalDate.now()
        );

        applicationRepository.save(
                application
        );

        System.out.println(
                "APPLY API CALLED"
        );
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

    public List<Application> getUserApplications(String mobileNumber) {
//        return applicationRepository.findByUser_MobileNumber(mobileNumber);
        return applicationRepository.findByMobileNumber(mobileNumber);
    }


    public Application updateStatus(Long id, ApplicationStatus status) {

        System.out.println("UPDATE STATUS METHOD CALLED");

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(status);

        Application savedApplication = applicationRepository.save(application);

        System.out.println("APPLICATION STATUS UPDATED");

        User user = userRepository.findByMobileNumber(application.getMobileNumber())
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

    public List<Application> getByUser(String mobileNumber) {
//        return applicationRepository.findByUser_MobileNumber(mobileNumber);
        return applicationRepository.findByMobileNumber(mobileNumber);
    }

    public long countByUser(String mobileNumber) {
//        return applicationRepository.countByUser_MobileNumber(mobileNumber);
        return applicationRepository.countByMobileNumber(mobileNumber);
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
