package com.edutrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String name;
    private Long gradeId;
    private String gradeName;
    
    private Long academicLevelId;
    private String academicLevelName;
    private Long institutionId;
    private String institutionName;
}