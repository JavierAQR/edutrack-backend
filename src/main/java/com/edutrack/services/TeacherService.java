package com.edutrack.services;

import com.edutrack.dto.request.TeacherCreateDTO;
import com.edutrack.dto.request.TeacherUpdateDTO;
import com.edutrack.dto.TeacherInfoDTO;
import com.edutrack.entities.TeacherProfile;
import com.edutrack.entities.User;
import com.edutrack.entities.enums.UserType;
import com.edutrack.repositories.TeacherProfileRepository;
import com.edutrack.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final UserRepository userRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public List<TeacherInfoDTO> getAllTeachers() {
        List<User> teachers = userRepository.findByUserType(UserType.TEACHER);

        return teachers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public TeacherInfoDTO createTeacher(TeacherCreateDTO teacherDTO) {
        // Crear usuario
        User user = new User();
        user.setUsername(teacherDTO.getUsername());
        user.setName(teacherDTO.getName());
        user.setLastname(teacherDTO.getLastname());
        user.setEmail(teacherDTO.getEmail());
        user.setPassword(passwordEncoder.encode(teacherDTO.getPassword()));
        user.setBirthdate(teacherDTO.getBirthdate());
        user.setEnabled(true);
        user.setUserType(UserType.TEACHER);

        User savedUser = userRepository.save(user);

        // Crear perfil
        TeacherProfile profile = new TeacherProfile();
        profile.setUser(savedUser);
        profile.setTitle(teacherDTO.getDegree());
        profile.setSpecialization(teacherDTO.getSpecialization());
        profile.setYearsExperience(teacherDTO.getTeachingExperience());
        profile.setBiography(teacherDTO.getBiography());
        profile.setCvUrl(teacherDTO.getCvUrl());

        teacherProfileRepository.save(profile);

        return convertToDTO(savedUser);
    }

    @Transactional
    public TeacherInfoDTO updateTeacher(Long id, TeacherUpdateDTO teacherDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setName(teacherDTO.getName());
        user.setLastname(teacherDTO.getLastname());
        user.setEmail(teacherDTO.getEmail());
        user.setBirthdate(teacherDTO.getBirthdate());
        user.setEnabled(teacherDTO.getEnabled());

        TeacherProfile profile = teacherProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Perfil de profesor no encontrado"));

        profile.setTitle(teacherDTO.getDegree());
        profile.setSpecialization(teacherDTO.getSpecialization());
        profile.setYearsExperience(teacherDTO.getTeachingExperience());
        profile.setBiography(teacherDTO.getBiography());
        profile.setCvUrl(teacherDTO.getCvUrl());

        userRepository.save(user);
        teacherProfileRepository.save(profile);

        return convertToDTO(user);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        teacherProfileRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    private TeacherInfoDTO convertToDTO(User user) {
        TeacherProfile profile = teacherProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Perfil de profesor no encontrado"));

        return TeacherInfoDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .birthdate(user.getBirthdate().toString())
                .enabled(user.getEnabled())
                .userType(user.getUserType().toString())
                .degree(profile.getTitle())
                .specialization(profile.getSpecialization())
                .teachingExperience(profile.getYearsExperience())
                .biography(profile.getBiography())
                .cvUrl(profile.getCvUrl())
                .build();
    }
}