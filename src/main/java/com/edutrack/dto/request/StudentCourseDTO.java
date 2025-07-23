package com.edutrack.dto.request;

public record StudentCourseDTO(
        Long id,
        String name,
        String teacherName,
        String period
) {}