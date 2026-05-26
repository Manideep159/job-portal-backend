package com.example.Status.of.application.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    private String title;

    public String getCompany() {
        return company;
    }

    public String getTitle() {
        return title;
    }

    private String company;

    private String sourceName;
    private String sourceUrl;
    private String externalJobId;
    private Boolean externalJob;

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getExternalJobId() {
        return externalJobId;
    }

    public void setExternalJobId(String externalJobId) {
        this.externalJobId = externalJobId;
    }

    public Boolean getExternalJob() {
        return externalJob;
    }

    public void setExternalJob(Boolean externalJob) {
        this.externalJob = externalJob;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setApplyLink(String applyLink) {
        this.applyLink = applyLink;
    }

    public void setLastDate(LocalDate lastDate) {
        this.lastDate = lastDate;
    }

    @Enumerated(EnumType.STRING)
    private JobType type;
    private String location;

    @Column(length = 2000)
    private String description;

    private String applyLink;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastDate;
}
