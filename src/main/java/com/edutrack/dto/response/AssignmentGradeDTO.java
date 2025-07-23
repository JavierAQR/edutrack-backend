package com.edutrack.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentGradeDTO {
    private Long assignmentId;
    private String title;
    private String fileUrl;
    private Double grade;
    private LocalDateTime submittedAt;
}