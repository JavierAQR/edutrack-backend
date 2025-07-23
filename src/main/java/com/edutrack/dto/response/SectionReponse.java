package com.edutrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionReponse {
    private Long id;
    private String name;
    private String courseName;
    private String academicLevelName;
    private String gradeName;
}
