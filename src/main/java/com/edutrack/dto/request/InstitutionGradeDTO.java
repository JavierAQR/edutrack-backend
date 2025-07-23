package com.edutrack.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionGradeDTO {

    private Long id;

    private Long institutionId;
    private String institutionName;

    private Long academicLevelId;
    private String academicLevelName;

    private Long gradeId;
    private String gradeName;
}