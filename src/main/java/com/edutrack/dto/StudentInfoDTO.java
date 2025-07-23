package com.edutrack.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentInfoDTO {
    private Long id;
    private String username;
    private String name;
    private String lastname;
    private String email;
    private String birthdate;
    private Boolean enabled;
    private String userType;
    private Long gradeId;
    private String gradeName;
    private String academicLevel;
}