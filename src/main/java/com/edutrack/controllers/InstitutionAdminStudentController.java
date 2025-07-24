package com.edutrack.controllers;

import com.edutrack.dto.response.admin.AdminStudentDTO;
import com.edutrack.dto.request.admin.AdminStudentRequestDTO;
import com.edutrack.dto.response.admin.AdminStudentDetailDTO;
import com.edutrack.services.InstitutionAdminStudentService;
import com.edutrack.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institution-admin/students")
@RequiredArgsConstructor
public class InstitutionAdminStudentController {

    private final InstitutionAdminStudentService studentService;

    @GetMapping
    public ResponseEntity<List<AdminStudentDTO>> getAllStudentsForInstitution(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);

        return ResponseEntity.ok(studentService.getStudentsByInstitution(institutionId));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<AdminStudentDetailDTO> getStudentDetails(
            @PathVariable Long studentId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);

        return ResponseEntity.ok(studentService.getStudentDetails(institutionId, studentId));
    }

    @PostMapping
    public ResponseEntity<AdminStudentDTO> createStudent(
            @RequestBody AdminStudentRequestDTO requestDTO,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(institutionId, requestDTO));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<AdminStudentDTO> updateStudent(
            @PathVariable Long studentId,
            @RequestBody AdminStudentRequestDTO requestDTO) {
        return ResponseEntity.ok(studentService.updateStudent(studentId, requestDTO));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}
