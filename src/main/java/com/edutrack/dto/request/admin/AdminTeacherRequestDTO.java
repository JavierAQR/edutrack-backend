package com.edutrack.dto.request.admin;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Data
public class AdminTeacherRequestDTO {
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

    @NotBlank
    private String title;

    @NotBlank
    private String specialization;

    @Min(0)
    private Integer yearsExperience;

    private String biography;

    @URL
    private String cvUrl;

    private Boolean enabled = true;
}