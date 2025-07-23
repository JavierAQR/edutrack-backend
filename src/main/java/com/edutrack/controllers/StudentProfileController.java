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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.ApiResponse;
import com.edutrack.dto.request.StudentProfileDTO;
import com.edutrack.dto.response.StudentByGradeResponse;
import com.edutrack.dto.response.StudentProfileResponse;
import com.edutrack.dto.response.StudentProfileResponseDTO;
import com.edutrack.entities.StudentProfile;
import com.edutrack.entities.User;
import com.edutrack.entities.enums.UserType;
import com.edutrack.repositories.StudentProfileRepository;
import com.edutrack.repositories.UserRepository;
import com.edutrack.services.StudentProfileService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student-profile")
@RequiredArgsConstructor
public class StudentProfileController {
    private final StudentProfileService studentProfileService;
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createProfile(
            @RequestBody @Valid StudentProfileDTO profileDTO,
            Authentication authentication) {

        try {

            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            StudentProfile profile = studentProfileService.createProfile(user.getId(), profileDTO);

            return ResponseEntity.ok(new ApiResponse("Perfil de estudiante creado exitosamente", profile));

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

            // Verificar que es un estudiante
            if (user.getUserType() != UserType.STUDENT) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Solo los estudiantes pueden acceder a esta información"));
            }

            Optional<StudentProfileResponseDTO> profile = studentProfileService.getProfileByUserId(user.getId());

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

            boolean hasProfile = studentProfileService.hasCompleteProfile(user.getId());

            Map<String, Object> response = Map.of(
                    "hasCompleteProfile", hasProfile,
                    "userType", user.getUserType().toString());

            return ResponseEntity.ok(new ApiResponse("Estado del perfil", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error interno del servidor"));
        }
    }

    @PutMapping("/update-student-info")
    @Transactional
    public ResponseEntity<?> updateProfessionalInfo(
            @RequestBody @Valid StudentProfileDTO profileDTO,
            Authentication authentication) {

        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Verificar que es un estudiante
            if (user.getUserType() != UserType.STUDENT) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Solo los estudiantes pueden actualizar información estudiantil"));
            }

            StudentProfileResponseDTO updatedProfile = studentProfileService.updateProfile(user.getId(), profileDTO);

            return ResponseEntity
                    .ok(new ApiResponse("Información estudiantil actualizada exitosamente", updatedProfile));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error interno del servidor"));
        }
    }

    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<StudentProfileResponse>> getStudentsByInstitution(@PathVariable Long institutionId) {
        List<StudentProfileResponse> result = studentProfileRepository.findByUser_Institution_Id(institutionId)
                .stream()
                .map(s -> new StudentProfileResponse(s.getId(),
                        s.getUser().getName() + " " + s.getUser().getLastname()))
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-grade-and-institution")
    public List<StudentByGradeResponse> getByGradeAndInstitution(
            @RequestParam Long gradeId,
            @RequestParam Long institutionId) {
        return studentProfileRepository.findByGradeAndInstitution(gradeId, institutionId);
    }
}
