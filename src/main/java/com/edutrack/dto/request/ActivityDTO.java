package com.edutrack.dto.request;

public record ActivityDTO(
        Long id,
        String title,
        boolean completed,
        String course,
        String date,
        String type
) {}
