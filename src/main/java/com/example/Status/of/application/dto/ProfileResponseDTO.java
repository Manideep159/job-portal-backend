package com.example.Status.of.application.dto;

public class ProfileResponseDTO {

    private String mobile;

    private String role;

    private String userName;

    private String email;

    private String resumePath;

    private String location;

    private long totalApplications;

    private long savedJobs;


    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }

    public ProfileResponseDTO(
            String mobile,
            String role,
            String userName,
            String email,
            String resumePath,
            String location,
            long totalApplications,
            long savedJobs
    ) {
        this.mobile = mobile;
        this.role = role;
        this.userName = userName;
        this.email = email;
        this.resumePath = resumePath;
        this.location = location;
        this.totalApplications = totalApplications;
        this.savedJobs = savedJobs;

    }

    public String getMobile() {
        return mobile;
    }

    public String getRole() {
        return role;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public long getSavedJobs() {
        return savedJobs;
    }
}
