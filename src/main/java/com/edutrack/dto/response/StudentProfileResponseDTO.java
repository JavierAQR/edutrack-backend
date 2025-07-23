package com.edutrack.dto.response;

import com.edutrack.entities.Grade;
import com.edutrack.entities.StudentProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfileResponseDTO {
    private Long id;
    private Grade grade;
    private String biography;

    public StudentProfileResponseDTO(StudentProfile studentProfile) {
        this.id = studentProfile.getId();
        this.grade = studentProfile.getGrade();
        this.biography = studentProfile.getBiography();

    }
}
