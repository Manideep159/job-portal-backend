package com.example.Status.of.application.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.Status.of.application.dto.ProfileResponseDTO;
import com.example.Status.of.application.dto.UpdateProfileDTO;
import com.example.Status.of.application.entity.User;
import com.example.Status.of.application.repository.ApplicationRepository;
import com.example.Status.of.application.repository.SavedJobRepository;
import com.example.Status.of.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private SavedJobRepository savedJobRepository;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/profile")
    public ProfileResponseDTO getProfile(
            Authentication authentication
    ) {

        String mobileNumber = authentication.getName();

        User user = userRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow();

        long totalApplications =
                applicationRepository.countByMobileNumber(mobileNumber);


        long savedJobs =
                savedJobRepository
                        .countByUserMobile(mobileNumber);

        return new ProfileResponseDTO(
                user.getMobileNumber(),
                user.getRole(),
                user.getName(),
                user.getEmail(),
                user.getResumePath(),
                user.getLocation(),
                totalApplications,
                savedJobs
        );
    }

    @PostMapping("/profile/upload-resume")
    public ResponseEntity<?> uploadResume(
            @RequestParam("resume") MultipartFile resume,
            Authentication authentication
    ) throws IOException {

        User user = userRepository
                .findByMobileNumber(authentication.getName())
                .orElseThrow();

        if (resume.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Please select a file");
        }

        String originalName = resume.getOriginalFilename();

        if (originalName == null ||
                !(originalName.endsWith(".pdf")
                        || originalName.endsWith(".doc")
                        || originalName.endsWith(".docx"))) {

            return ResponseEntity.badRequest()
                    .body("Only PDF, DOC and DOCX files are allowed");
        }

        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                resume.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "raw",
                        "folder", "jobportal/resumes",
                        "public_id",
                        System.currentTimeMillis() + "_" + originalName
                )
        );

        String resumeUrl =
                uploadResult.get("secure_url").toString();

        user.setResumePath(resumeUrl);

        userRepository.save(user);

        return ResponseEntity.ok(resumeUrl);
    }

//    @PostMapping("/profile/upload-resume")
//    public ResponseEntity<?> uploadResume(
//            @RequestParam("resume") MultipartFile resume,
//            Authentication authentication
//    ) throws IOException {
//
//        User user =
//                userRepository.findByMobileNumber(
//                        authentication.getName()
//                ).orElseThrow();
//
//        String fileName =
//                System.currentTimeMillis()
//                        + "_"
//                        + resume.getOriginalFilename();
//
//        Path uploadPath =
//                Paths.get("uploads");
//
//        Files.createDirectories(uploadPath);
//
//        Files.copy(
//                resume.getInputStream(),
//                uploadPath.resolve(fileName),
//                StandardCopyOption.REPLACE_EXISTING
//        );
//
//        user.setResumePath(fileName);
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok(
//                "Resume uploaded successfully"
//        );
//    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody UpdateProfileDTO dto,
            Authentication authentication
    ) {

        String mobile = authentication.getName();

        User user = userRepository
                .findByMobileNumber(mobile)
                .orElseThrow();

        user.setName(dto.getUserName());

        user.setEmail(dto.getEmail());

        user.setLocation(dto.getLocation());

        userRepository.save(user);

        return ResponseEntity.ok("Profile updated");
    }
}
