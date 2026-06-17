package com.example.Status.of.application.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
//import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
//@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mobile_number", unique = true)
    private String mobileNumber;

    @Column(name = "resume_path")
    private String resumePath;

    @Column(name = "user_name")
    private String name;

    private String tokenVersion;

    private String role; // ROLE_USER or ROLE_ADMIN

    private String email;

    private String location;

    public String getTokenVersion() {
        return tokenVersion;
    }

    public void setTokenVersion(String tokenVersion) {
        this.tokenVersion = tokenVersion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String loaction) {
        this.location = loaction;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }

    public String getResumePath() {
        return resumePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // constructor
    public User(Long id, String mobileNumber, String name, String role) {
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.role = role;
    }
}



