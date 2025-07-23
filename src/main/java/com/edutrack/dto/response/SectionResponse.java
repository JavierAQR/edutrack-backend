package com.edutrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionResponse {
    private Long id;
    private String name;

    private Long courseId;
    private String courseName;

    private Long teacherId;
    private String teacherFullName;

    private Long gradeId;
    private String gradeName;

    private Long academicLevelId;
    private String academicLevelName;

    private Long institutionId;
    private String institutionName;
}
