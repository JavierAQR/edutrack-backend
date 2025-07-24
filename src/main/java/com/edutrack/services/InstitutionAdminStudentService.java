package com.edutrack.services;

import com.edutrack.dto.response.admin.AdminStudentDTO;
import com.edutrack.dto.request.admin.AdminStudentRequestDTO;
import com.edutrack.dto.response.admin.AdminStudentDetailDTO;
import com.edutrack.entities.*;
import com.edutrack.entities.enums.UserType;
import com.edutrack.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionAdminStudentService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final InstitutionRepository institutionRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AdminStudentDTO> getStudentsByInstitution(Long institutionId) {
        return userRepository.findByInstitutionIdAndUserTypeWithProfile(institutionId, UserType.STUDENT)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdminStudentDTO createStudent(Long institutionId, AdminStudentRequestDTO requestDTO) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        Grade grade = null;
        if (requestDTO.getGradeId() != null) {
            grade = gradeRepository.findById(requestDTO.getGradeId())
                    .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
        }

        // Crear usuario
        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setName(requestDTO.getName());
        user.setLastname(requestDTO.getLastname());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setBirthdate(requestDTO.getBirthdate());
        user.setInstitution(institution);
        user.setUserType(UserType.STUDENT);
        user.setEnabled(requestDTO.getEnabled());

        user = userRepository.save(user);

        // Crear perfil de estudiante
        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        profile.setGrade(grade);
        profile.setBiography(requestDTO.getBiography() != null ? requestDTO.getBiography() : "");

        studentProfileRepository.save(profile);

        return convertToDTO(user);
    }

    @Transactional
    public AdminStudentDTO updateStudent(Long studentId, AdminStudentRequestDTO requestDTO) {
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        user.setName(requestDTO.getName());
        user.setLastname(requestDTO.getLastname());
        user.setEmail(requestDTO.getEmail());
        user.setBirthdate(requestDTO.getBirthdate());
        user.setEnabled(requestDTO.getEnabled());

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        StudentProfile profile = user.getStudentProfile();
        if (profile == null) {
            profile = new StudentProfile();
            profile.setUser(user);
        }

        Grade grade = null;
        if (requestDTO.getGradeId() != null) {
            grade = gradeRepository.findById(requestDTO.getGradeId())
                    .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
        }
        profile.setGrade(grade);
        profile.setBiography(requestDTO.getBiography() != null ? requestDTO.getBiography() : "");

        studentProfileRepository.save(profile);
        user = userRepository.save(user);

        return convertToDTO(user);
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        if (user.getStudentProfile() != null) {
            studentProfileRepository.delete(user.getStudentProfile());
        }

        userRepository.delete(user);
    }

    private AdminStudentDTO convertToDTO(User user) {
        AdminStudentDTO dto = new AdminStudentDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setFullName(user.getName() + " " + user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setBirthdate(user.getBirthdate());
        dto.setStatus(user.getEnabled() ? "ACTIVE" : "INACTIVE");
        dto.setHasCompleteProfile(user.hasCompleteProfile());

        if (user.getStudentProfile() != null) {
            StudentProfile profile = user.getStudentProfile();
            dto.setBiography(profile.getBiography()); // Añadir la biografía

            if (profile.getGrade() != null) {
                dto.setGradeName(profile.getGrade().getName());
                if (profile.getGrade().getAcademicLevel() != null) {
                    dto.setAcademicLevel(profile.getGrade().getAcademicLevel().getName());
                }
            }
        }

        return dto;
    }

    public AdminStudentDetailDTO getStudentDetails(Long institutionId, Long studentId) {
        User user = userRepository.findByInstitutionIdAndIdAndUserType(institutionId, studentId, UserType.STUDENT)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado o no pertenece a esta institución"));

        return convertToDetailDTO(user);
    }

    private AdminStudentDetailDTO convertToDetailDTO(User user) {
        AdminStudentDetailDTO dto = new AdminStudentDetailDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setBirthdate(user.getBirthdate());
        dto.setEnabled(user.getEnabled());

        if (user.getStudentProfile() != null) {
            StudentProfile profile = user.getStudentProfile();
            dto.setBiography(profile.getBiography());

            if (profile.getGrade() != null) {
                dto.setGradeId(profile.getGrade().getId());
                dto.setGradeName(profile.getGrade().getName());
                if (profile.getGrade().getAcademicLevel() != null) {
                    dto.setAcademicLevel(profile.getGrade().getAcademicLevel().getName());
                }
            }
        }

        return dto;
    }
}
