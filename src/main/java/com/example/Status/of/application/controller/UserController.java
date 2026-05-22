package com.example.Status.of.application.controller;

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

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private SavedJobRepository savedJobRepository;

    @GetMapping("/profile")
    public ProfileResponseDTO getProfile(
            Authentication authentication
    ) {

        String mobile = authentication.getName();

        User user = userRepository
                .findByMobileNumber(mobile)
                .orElseThrow();

        long totalApplications =
                applicationRepository
                        .countByUserMobile(mobile);

        long savedJobs =
                savedJobRepository
                        .countByUserMobile(mobile);

        return new ProfileResponseDTO(
                user.getMobileNumber(),
                user.getRole(),
                user.getName(),
                user.getEmail(),
                user.getLocation(),
                totalApplications,
                savedJobs
        );
    }

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
