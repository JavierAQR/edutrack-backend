package com.edutrack.dto.request.admin;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminStudentRequestDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @Email
    @NotBlank
    private String email;

    @Size(min = 6)
    private String password;

    @NotNull
    private LocalDate birthdate;

    private Long gradeId;

    private String biography;

    private Boolean enabled = true;
}