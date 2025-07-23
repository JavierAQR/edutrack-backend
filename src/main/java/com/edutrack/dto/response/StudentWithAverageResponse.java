package com.edutrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentWithAverageResponse {
    private Long studentId;
    private String studentName;
    private Double averageGrade;
}
