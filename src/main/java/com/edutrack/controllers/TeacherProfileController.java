package com.edutrack.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.ApiResponse;
import com.edutrack.dto.request.TeacherProfileDTO;
import com.edutrack.dto.response.TeacherProfileResponse;
import com.edutrack.dto.response.TeacherProfileResponseDTO;
import com.edutrack.entities.TeacherProfile;
import com.edutrack.entities.User;
import com.edutrack.entities.enums.UserType;
import com.edutrack.repositories.TeacherProfileRepository;
import com.edutrack.repositories.UserRepository;
import com.edutrack.services.TeacherProfileService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teacher-profile")
@RequiredArgsConstructor
public class TeacherProfileController {

    private final TeacherProfileService teacherProfileService;
    private final UserRepository userRepository;
    private final TeacherProfileRepository teacherProfileRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createProfile(
            @RequestBody @Valid TeacherProfileDTO profileDTO,
            Authentication authentication) {

        try {

            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            TeacherProfile profile = teacherProfileService.createProfile(user.getId(), profileDTO);

            return ResponseEntity.ok(new ApiResponse("Perfil de profesor creado exitosamente", profile));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error interno del servidor"));
        }
    }

    @GetMapping("/my-profile")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar que es un profesor
            if (user.getUserType() != UserType.TEACHER) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Solo los profesores pueden acceder a esta información"));
            }

            Optional<TeacherProfileResponseDTO> profile = teacherProfileService.getProfileByUserId(user.getId());

            if (profile.isPresent()) {
                return ResponseEntity.ok(new ApiResponse("Perfil encontrado", profile.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Perfil no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error interno del servidor"));
        }
    }

    @GetMapping("/has-profile")
    public ResponseEntity<?> hasProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            boolean hasProfile = teacherProfileService.hasCompleteProfile(user.getId());

            Map<String, Object> response = Map.of(
                    "hasCompleteProfile", hasProfile,
                    "userType", user.getUserType().toString());

            return ResponseEntity.ok(new ApiResponse("Estado del perfil", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error interno del servidor"));
        }
    }

    @PutMapping("/update-professional-info")
    @Transactional
    public ResponseEntity<?> updateProfessionalInfo(
            @RequestBody @Valid TeacherProfileDTO profileDTO,
            Authentication authentication) {

        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar que es un profesor
            if (user.getUserType() != UserType.TEACHER) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Solo los profesores pueden actualizar información profesional"));
            }

            TeacherProfileResponseDTO updatedProfile = teacherProfileService.updateProfile(user.getId(), profileDTO);

            return ResponseEntity
                    .ok(new ApiResponse("Información profesional actualizada exitosamente", updatedProfile));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error interno del servidor"));
        }
    }

    @GetMapping("/institution/{institutionId}")
public ResponseEntity<List<TeacherProfileResponse>> getTeachersByInstitution(@PathVariable Long institutionId) {
    List<TeacherProfileResponse> result = teacherProfileRepository.findByUser_Institution_Id(institutionId)
        .stream()
        .map(t -> new TeacherProfileResponse(t.getId(), t.getUser().getName() + " " + t.getUser().getLastname()))
        .toList();

    return ResponseEntity.ok(result);
}

}