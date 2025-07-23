package com.edutrack.dto.request;

public record StudentActivityDTO(
        Long id,
        String title,
        boolean completed,
        String courseName,
        String dueDate
) {}
