package com.edutrack.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutrack.dto.request.StudentProfileDTO;
import com.edutrack.dto.response.StudentProfileResponseDTO;
import com.edutrack.entities.Grade;
import com.edutrack.entities.StudentProfile;
import com.edutrack.entities.User;
import com.edutrack.entities.enums.UserType;
import com.edutrack.repositories.GradeRepository;
import com.edutrack.repositories.StudentProfileRepository;
import com.edutrack.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StudentProfileService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private UserRepository userRepository;

    public StudentProfile createProfile(Long userId, StudentProfileDTO profileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getUserType() != UserType.STUDENT) {
            throw new IllegalArgumentException("El usuario no es un profesor");
        }

        if (studentProfileRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("El estudiante ya tiene un perfil creado");
        }

        Grade grade = gradeRepository.findById(profileDTO.getGradeId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        profile.setGrade(grade);
        profile.setBiography(profileDTO.getBiography());

        return studentProfileRepository.save(profile);
    }

    public Optional<StudentProfileResponseDTO> getProfileByUserId(Long userId) {
        return studentProfileRepository.findByUserId(userId)
                .map(StudentProfileResponseDTO::new);
    }

    public StudentProfileResponseDTO updateProfile(Long userId, StudentProfileDTO profileDTO) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de estudiante no encontrado"));

        Grade grade = gradeRepository.findById(profileDTO.getGradeId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        profile.setGrade(grade);
        profile.setBiography(profileDTO.getBiography());

        StudentProfile updatedProfile = studentProfileRepository.save(profile);

        return new StudentProfileResponseDTO(updatedProfile);
    }

    public boolean hasCompleteProfile(Long userId) {
        return studentProfileRepository.existsByUserId(userId);
    }

}
