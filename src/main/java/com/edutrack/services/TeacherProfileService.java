package com.edutrack.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutrack.dto.request.TeacherProfileDTO;
import com.edutrack.dto.response.TeacherProfileResponseDTO;
import com.edutrack.entities.TeacherProfile;
import com.edutrack.entities.User;
import com.edutrack.entities.enums.UserType;
import com.edutrack.repositories.TeacherProfileRepository;
import com.edutrack.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TeacherProfileService {
    
    @Autowired
    private TeacherProfileRepository teacherProfileRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public TeacherProfile createProfile(Long userId, TeacherProfileDTO profileDTO) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (user.getUserType() != UserType.TEACHER) {
            throw new IllegalArgumentException("El usuario no es un profesor");
        }
        
        if (teacherProfileRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("El profesor ya tiene un perfil creado");
        }
        
        TeacherProfile profile = new TeacherProfile();
        profile.setUser(user);
        profile.setSpecialization(profileDTO.getSpecialization());
        profile.setTitle(profileDTO.getTitle());
        profile.setYearsExperience(profileDTO.getYearsExperience());
        profile.setBiography(profileDTO.getBiography());
        profile.setCvUrl(profileDTO.getCvUrl());
        
        return teacherProfileRepository.save(profile);
    }
    
    public Optional<TeacherProfileResponseDTO> getProfileByUserId(Long userId) {
        return teacherProfileRepository.findByUserId(userId)
               .map(TeacherProfileResponseDTO::new);
    }
    
    public TeacherProfileResponseDTO updateProfile(Long userId, TeacherProfileDTO profileDTO) {
        TeacherProfile profile = teacherProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Perfil de profesor no encontrado"));
        
        profile.setSpecialization(profileDTO.getSpecialization());
        profile.setTitle(profileDTO.getTitle());
        profile.setYearsExperience(profileDTO.getYearsExperience());
        profile.setBiography(profileDTO.getBiography());
        profile.setCvUrl(profileDTO.getCvUrl());
        
        TeacherProfile updatedProfile = teacherProfileRepository.save(profile);
        
        // Retornar solo los datos del perfil, sin el usuario anidado
        return new TeacherProfileResponseDTO(updatedProfile);
    }
    
    public boolean hasCompleteProfile(Long userId) {
        return teacherProfileRepository.existsByUserId(userId);
    }
    
}
