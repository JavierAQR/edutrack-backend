package com.edutrack.dto.response.admin;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminStudentDetailDTO {
    private Long id;
    private String username;
    private String name;
    private String lastname;
    private String email;
    private LocalDate birthdate;
    private Long gradeId;
    private String gradeName;
    private String academicLevel;
    private String biography;
    private Boolean enabled;
}