package com.edutrack.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class InstitutionDTO {
    private Long id;
    private String name;
    private String address;
    private String description;
    private String phone;
    private String website;
    private List<String> academicLevels;
}
