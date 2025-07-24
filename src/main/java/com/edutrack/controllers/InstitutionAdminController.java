package com.edutrack.controllers;

import com.edutrack.entities.AcademicLevel;
import com.edutrack.entities.Grade;
import com.edutrack.entities.InstitutionGrade;
import com.edutrack.services.GradeService;
import com.edutrack.services.InstitutionAdminService;

import com.edutrack.services.InstitutionService;
import com.edutrack.util.JwtUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institution-admin")
@RequiredArgsConstructor
public class InstitutionAdminController {

    @Autowired
    private final InstitutionAdminService institutionAdminService;
    private final InstitutionService institutionService;
    private final GradeService gradeService;

    @GetMapping("/grades")
    public List<InstitutionGrade> getGradesForCurrentInstitution() {
        return institutionAdminService.getGradesForCurrentAdminInstitution();
    }

    @GetMapping("/academic-levels")
    public ResponseEntity<List<AcademicLevel>> getAcademicLevelsByInstitution(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);

        List<AcademicLevel> levels = institutionService.getAcademicLevelsByInstitution(institutionId);
        return ResponseEntity.ok(levels);
    }

    @GetMapping("/grades-by-level/{academicLevelId}")
    public ResponseEntity<List<Grade>> getGradesByAcademicLevel(
            @PathVariable Long academicLevelId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        Long institutionId = JwtUtils.extractInstitutionId(token);

        List<Grade> grades = gradeService.getGradesByAcademicLevelAndInstitution(academicLevelId, institutionId);
        return ResponseEntity.ok(grades);
    }
}
