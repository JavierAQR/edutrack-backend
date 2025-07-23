package com.edutrack.dto.response;

import com.edutrack.entities.TeacherProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherProfileResponseDTO {
    private Long id;
    private String specialization;
    private String title;
    private Integer yearsExperience;
    private String biography;
    private String cvUrl;

    // Constructor desde la entidad
    public TeacherProfileResponseDTO(TeacherProfile teacherProfile) {
        this.id = teacherProfile.getId();
        this.specialization = teacherProfile.getSpecialization();
        this.title = teacherProfile.getTitle();
        this.yearsExperience = teacherProfile.getYearsExperience();
        this.biography = teacherProfile.getBiography();
        this.cvUrl = teacherProfile.getCvUrl();

    }
}
