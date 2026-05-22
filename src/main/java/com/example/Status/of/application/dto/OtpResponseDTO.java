package com.example.Status.of.application.dto;


public class OtpResponseDTO {

    private String token;

    private String role;

    public OtpResponseDTO() {
    }

    public OtpResponseDTO(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
