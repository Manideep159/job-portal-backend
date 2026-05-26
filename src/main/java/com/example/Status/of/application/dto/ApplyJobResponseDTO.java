package com.example.Status.of.application.dto;


public class ApplyJobResponseDTO {

    private String message;
    private String applyLink;

    public ApplyJobResponseDTO(String message, String applyLink) {
        this.message = message;
        this.applyLink = applyLink;
    }

    public String getMessage() {
        return message;
    }

    public String getApplyLink() {
        return applyLink;
    }
}
