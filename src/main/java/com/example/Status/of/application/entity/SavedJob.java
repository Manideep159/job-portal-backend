package com.example.Status.of.application.entity;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String userMobile;

    @Setter
    @ManyToOne
    private Job job;

    public SavedJob() {
    }

    public Long getId() {
        return id;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public Job getJob() {
        return job;
    }

}
