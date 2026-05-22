package com.example.Status.of.application.dto;

import lombok.Data;

@Data
public class JobRequestDTO {
    private String title;
    private String company;
    private String type;
    private String location;
    private String description;
    private String applyLink;
    private String lastDate;
}
