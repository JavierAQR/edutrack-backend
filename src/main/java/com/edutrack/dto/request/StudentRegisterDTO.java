package com.edutrack.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentRegisterDTO {
    @NotBlank
    private String name;
    
    @NotBlank
    private String lastname;
    
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;

    @NotNull
    private LocalDate birthdate;
    
    // Campos específicos de Student
    @NotNull
    private Long institutionId;
    
    @NotNull
    private Long academicLevelId;

    private Double averageGrade;
}
