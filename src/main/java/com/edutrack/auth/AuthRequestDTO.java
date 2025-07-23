package com.edutrack.auth;

import java.time.LocalDate;

import com.edutrack.entities.enums.UserType;

public record AuthRequestDTO(
    String name,
    String lastname,
    String username,
    String password,
    String email,
    Long institutionId,
    LocalDate birthdate,
    UserType userType
) {
    
}
