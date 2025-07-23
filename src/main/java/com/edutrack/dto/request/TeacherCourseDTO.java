package com.edutrack.dto.request;

import java.time.LocalTime;
import java.util.List;

public record TeacherCourseDTO(
        Long id,
        String name,
        String academicArea,
        AcademicLevelDTO academicLevel,
        List<SectionInfoDTO> sections
) {
    public record SectionInfoDTO(
            Long id,
            String schedule,
            LocalTime startTime,
            LocalTime endTime,
            int studentCount,
            String periodName
    ) {}
}