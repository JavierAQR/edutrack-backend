package com.edutrack.controllers;

import com.edutrack.dto.response.admin.AdminCourseDTO;
import com.edutrack.dto.request.admin.AdminCourseRequestDTO;
import com.edutrack.dto.response.admin.AdminGradeDTO;
import com.edutrack.services.InstitutionAdminCourseService;
import com.edutrack.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institution-admin/courses")
@RequiredArgsConstructor
public class InstitutionAdminCourseController {

    private final InstitutionAdminCourseService courseService;

    @GetMapping
    public ResponseEntity<List<AdminCourseDTO>> getAllCoursesForInstitution(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);
        return ResponseEntity.ok(courseService.getCoursesByInstitution(institutionId));
    }

    @GetMapping("/grades")
    public ResponseEntity<List<AdminGradeDTO>> getGradesForInstitution(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);
        return ResponseEntity.ok(courseService.getGradesByInstitution(institutionId));
    }

    @PostMapping
    public ResponseEntity<AdminCourseDTO> createCourse(
            @RequestBody AdminCourseRequestDTO requestDTO,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createCourse(institutionId, requestDTO));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<AdminCourseDTO> updateCourse(
            @PathVariable Long courseId,
            @RequestBody AdminCourseRequestDTO requestDTO,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);
        return ResponseEntity.ok(courseService.updateCourse(institutionId, courseId, requestDTO));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long courseId,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);
        courseService.deleteCourse(institutionId, courseId);
        return ResponseEntity.noContent().build();
    }
}