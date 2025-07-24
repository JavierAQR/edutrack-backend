package com.edutrack.dto.response.admin;

import lombok.Data;

@Data
public class AdminCourseDTO {
    private Long id;
    private String name;
    private Long gradeId;
    private String gradeName;
    private Long academicLevelId;
    private String academicLevelName;
}