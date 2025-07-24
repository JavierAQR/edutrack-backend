package com.edutrack.dto.response.admin;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminTeacherDTO {
    private Long id;
    private String username;
    private String name;
    private String lastname;
    private String fullName;
    private String email;
    private LocalDate birthdate;
    private String title;
    private String specialization;
    private Integer yearsExperience;
    private String biography;
    private String cvUrl;
    private String status;
    private Boolean hasCompleteProfile;
}