package com.edutrack.dto.response.admin;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminTeacherDetailDTO {
    private Long id;
    private String username;
    private String name;
    private String lastname;
    private String email;
    private LocalDate birthdate;
    private String title;
    private String specialization;
    private Integer yearsExperience;
    private String biography;
    private String cvUrl;
    private Boolean enabled;
}