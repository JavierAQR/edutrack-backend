package com.edutrack.dto.response.admin;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminStudentDTO {
    private Long id;
    private String username;
    private String name;
    private String lastname;
    private String fullName;
    private String email;
    private LocalDate birthdate;
    private String gradeName;
    private String academicLevel;
    private String status;
    private Boolean hasCompleteProfile;
    private String biography;
}