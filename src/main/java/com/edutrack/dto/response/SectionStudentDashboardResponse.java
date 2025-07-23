package com.edutrack.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionStudentDashboardResponse {
    private Long sectionId;
    private String sectionName;
    private String courseName;
    private String teacherName;

    private Double averageGrade;
    private List<AssignmentGradeDTO> submittedAssignments;
    private List<AssignmentDTO> pendingAssignments;
}
