package com.edutrack.services;

import com.edutrack.dto.response.admin.AdminTeacherDTO;
import com.edutrack.dto.request.admin.AdminTeacherRequestDTO;
import com.edutrack.dto.response.admin.AdminTeacherDetailDTO;
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
public class InstitutionAdminTeacherService {

    private final UserRepository userRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final InstitutionRepository institutionRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AdminTeacherDTO> getTeachersByInstitution(Long institutionId) {
        return userRepository.findByInstitutionIdAndUserType(institutionId, UserType.TEACHER)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdminTeacherDTO createTeacher(Long institutionId, AdminTeacherRequestDTO requestDTO) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        // Crear usuario
        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setName(requestDTO.getName());
        user.setLastname(requestDTO.getLastname());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setBirthdate(requestDTO.getBirthdate());
        user.setInstitution(institution);
        user.setUserType(UserType.TEACHER);
        user.setEnabled(requestDTO.getEnabled());

        user = userRepository.save(user);

        // Crear perfil de profesor
        TeacherProfile profile = new TeacherProfile();
        profile.setUser(user);
        profile.setTitle(requestDTO.getTitle());
        profile.setSpecialization(requestDTO.getSpecialization());
        profile.setYearsExperience(requestDTO.getYearsExperience());
        profile.setBiography(requestDTO.getBiography());
        profile.setCvUrl(requestDTO.getCvUrl());

        teacherProfileRepository.save(profile);

        return convertToDTO(user);
    }

    @Transactional
    public AdminTeacherDTO updateTeacher(Long teacherId, AdminTeacherRequestDTO requestDTO) {
        User user = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        user.setName(requestDTO.getName());
        user.setLastname(requestDTO.getLastname());
        user.setEmail(requestDTO.getEmail());
        user.setBirthdate(requestDTO.getBirthdate());
        user.setEnabled(requestDTO.getEnabled());

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        TeacherProfile profile = user.getTeacherProfile();
        if (profile == null) {
            profile = new TeacherProfile();
            profile.setUser(user);
        }

        profile.setTitle(requestDTO.getTitle());
        profile.setSpecialization(requestDTO.getSpecialization());
        profile.setYearsExperience(requestDTO.getYearsExperience());
        profile.setBiography(requestDTO.getBiography());
        profile.setCvUrl(requestDTO.getCvUrl());

        teacherProfileRepository.save(profile);
        user = userRepository.save(user);

        return convertToDTO(user);
    }

    @Transactional
    public void deleteTeacher(Long teacherId) {
        User user = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        if (user.getTeacherProfile() != null) {
            teacherProfileRepository.delete(user.getTeacherProfile());
        }

        userRepository.delete(user);
    }

    private AdminTeacherDTO convertToDTO(User user) {
        AdminTeacherDTO dto = new AdminTeacherDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setFullName(user.getName() + " " + user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setBirthdate(user.getBirthdate());
        dto.setStatus(user.getEnabled() ? "ACTIVE" : "INACTIVE");
        dto.setHasCompleteProfile(user.hasCompleteProfile());

        if (user.getTeacherProfile() != null) {
            TeacherProfile profile = user.getTeacherProfile();
            dto.setTitle(profile.getTitle());
            dto.setSpecialization(profile.getSpecialization());
            dto.setYearsExperience(profile.getYearsExperience());
            dto.setCvUrl(profile.getCvUrl());
            dto.setBiography(profile.getBiography());
        }

        return dto;
    }

    public AdminTeacherDetailDTO getTeacherDetails(Long institutionId, Long teacherId) {
        User user = userRepository.findByInstitutionIdAndIdAndUserType(institutionId, teacherId, UserType.TEACHER)
                .orElseThrow(() -> new EntityNotFoundException("Profesor no encontrado o no pertenece a esta institución"));

        return convertToDetailDTO(user);
    }

    private AdminTeacherDetailDTO convertToDetailDTO(User user) {
        AdminTeacherDetailDTO dto = new AdminTeacherDetailDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setBirthdate(user.getBirthdate());
        dto.setEnabled(user.getEnabled());

        if (user.getTeacherProfile() != null) {
            TeacherProfile profile = user.getTeacherProfile();
            dto.setTitle(profile.getTitle());
            dto.setSpecialization(profile.getSpecialization());
            dto.setYearsExperience(profile.getYearsExperience());
            dto.setBiography(profile.getBiography());
            dto.setCvUrl(profile.getCvUrl());
        }

        return dto;
    }
}