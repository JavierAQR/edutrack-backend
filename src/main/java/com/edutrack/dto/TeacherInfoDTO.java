package com.edutrack.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherInfoDTO {
    private Long id;
    private String username;
    private String name;
    private String lastname;
    private String email;
    private String birthdate;
    private Boolean enabled;
    private String userType;
    private String degree;
    private String biography;
    private String specialization;
    private Integer teachingExperience;
    private String cvUrl;
}