package com.edutrack.dto.request;

public record UserDetailsDTO(
        Long id,
        String name,
        String lastname,
        String email,
        String role,
        String code,
        String additionalInfo,
        String institutionName
) {}