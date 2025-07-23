package com.edutrack.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TeacherUpdateDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastname;

    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate birthdate;

    @NotNull(message = "El estado es obligatorio")
    private Boolean enabled;

    @NotBlank(message = "El título es obligatorio")
    private String degree;

    @NotBlank(message = "La especialización es obligatoria")
    private String specialization;

    @NotNull(message = "La experiencia es obligatoria")
    @Min(value = 0, message = "La experiencia no puede ser negativa")
    private Integer teachingExperience;

    private String biography;
    private String cvUrl;
}
