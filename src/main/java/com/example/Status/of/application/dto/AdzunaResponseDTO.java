package com.example.Status.of.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdzunaResponseDTO {

    private List<AdzunaJobDTO> results;
}
