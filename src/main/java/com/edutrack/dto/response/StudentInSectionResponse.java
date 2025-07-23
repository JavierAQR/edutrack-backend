package com.edutrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInSectionResponse {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String grade;
    private String academicLevel;
}
