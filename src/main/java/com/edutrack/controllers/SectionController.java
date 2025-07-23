package com.edutrack.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.request.AssignStudentsRequest;
import com.edutrack.dto.request.SectionRequest;
import com.edutrack.dto.response.SectionResponse;
import com.edutrack.dto.response.SectionStudentDashboardResponse;
import com.edutrack.dto.response.StudentInSectionResponse;
import com.edutrack.dto.response.StudentWithAverageResponse;
import com.edutrack.entities.Section;
import com.edutrack.services.SectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<Section> createSection(@RequestBody SectionRequest request) {
        Section section = sectionService.createSection(
                request.getCourseId(),
                request.getTeacherId(),
                request.getInstitutionId(),
                request.getName());
        return ResponseEntity.ok(section);
    }

    @GetMapping("/institution/{institutionId}")
    public ResponseEntity<List<SectionResponse>> getSectionsByInstitution(@PathVariable Long institutionId) {
        return ResponseEntity.ok(sectionService.getSectionsByInstitution(institutionId));
    }

    @PutMapping("/{sectionId}/students")
    public ResponseEntity<Section> assignStudents(
            @PathVariable Long sectionId,
            @RequestBody AssignStudentsRequest request) {
        Section updatedSection = sectionService.assignStudentsToSection(sectionId, request.getStudentIds());
        return ResponseEntity.ok(updatedSection);
    }

    @GetMapping("/{sectionId}/students")
    public ResponseEntity<List<StudentInSectionResponse>> getStudents(@PathVariable Long sectionId) {
        return ResponseEntity.ok(sectionService.getStudentsInSection(sectionId));
    }

    @GetMapping("/{sectionId}/students-averages")
    public ResponseEntity<List<StudentWithAverageResponse>> getStudentsWithAverages(@PathVariable Long sectionId) {
        return ResponseEntity.ok(sectionService.getStudentsWithAveragesInSection(sectionId));
    }

    @GetMapping("/dashboard/student/{studentId}")
    public ResponseEntity<List<SectionStudentDashboardResponse>> getStudentDashboard(@PathVariable Long studentId) {
        return ResponseEntity.ok(sectionService.getStudentSectionDashboard(studentId));
    }

    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<List<SectionResponse>> getSectionsByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(sectionService.getSectionsByTeacherId(teacherId));
    }
}
