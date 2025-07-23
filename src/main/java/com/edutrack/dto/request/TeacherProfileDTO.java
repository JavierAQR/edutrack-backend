package com.edutrack.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherProfileDTO {
    
    @NotBlank(message = "La especialización es requerida")
    private String specialization;
    
    @NotBlank(message = "El título es requerido")
    private String title;
    
    @NotNull(message = "Los años de experiencia son requeridos")
    @Min(value = 0, message = "Los años de experiencia no pueden ser negativos")
    private Integer yearsExperience;
    
    private String biography;

    @Pattern(regexp = "^(https?://).*", message = "La URL debe comenzar con http:// o https://")
    private String cvUrl;
    
    // @Pattern(regexp = "^[+]?[0-9\\s-()]+$", message = "Formato de teléfono inválido")
    // private String phoneNumber;
}
