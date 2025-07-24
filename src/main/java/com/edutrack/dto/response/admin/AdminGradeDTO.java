package com.edutrack.dto.response.admin;

import lombok.Data;

@Data
public class AdminGradeDTO {
    private Long id;
    private String name;
    private Long academicLevelId;
    private String academicLevelName;
}