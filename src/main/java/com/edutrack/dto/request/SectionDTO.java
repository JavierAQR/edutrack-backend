package com.edutrack.dto.request;

public record SectionDTO(
        Long id,
        String schedule,
        Integer studentCount
) {}