package com.edutrack.dto.request;

public record UserInfoDTO(
        String userType,
        String fullName,
        String email
) {}