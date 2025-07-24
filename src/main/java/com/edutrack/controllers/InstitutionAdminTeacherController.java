package com.edutrack.controllers;

import com.edutrack.dto.response.admin.AdminTeacherDTO;
import com.edutrack.dto.request.admin.AdminTeacherRequestDTO;
import com.edutrack.dto.response.admin.AdminTeacherDetailDTO;
import com.edutrack.services.InstitutionAdminTeacherService;
import com.edutrack.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institution-admin/teachers")
@RequiredArgsConstructor
public class InstitutionAdminTeacherController {

    private final InstitutionAdminTeacherService teacherService;

    @GetMapping
    public ResponseEntity<List<AdminTeacherDTO>> getAllTeachersForInstitution(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);

        return ResponseEntity.ok(teacherService.getTeachersByInstitution(institutionId));
    }

    @GetMapping("/{teacherId}")
    public ResponseEntity<AdminTeacherDetailDTO> getTeacherDetails(
            @PathVariable Long teacherId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);

        return ResponseEntity.ok(teacherService.getTeacherDetails(institutionId, teacherId));
    }

    @PostMapping
    public ResponseEntity<AdminTeacherDTO> createTeacher(
            @RequestBody AdminTeacherRequestDTO requestDTO,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teacherService.createTeacher(institutionId, requestDTO));
    }

    @PutMapping("/{teacherId}")
    public ResponseEntity<AdminTeacherDTO> updateTeacher(
            @PathVariable Long teacherId,
            @RequestBody AdminTeacherRequestDTO requestDTO) {
        return ResponseEntity.ok(teacherService.updateTeacher(teacherId, requestDTO));
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.noContent().build();
    }
}