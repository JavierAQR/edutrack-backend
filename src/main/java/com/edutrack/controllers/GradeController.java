package com.edutrack.controllers;

import com.edutrack.dto.ApiResponse;
import com.edutrack.entities.Grade;
import com.edutrack.services.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllGrades() {
        List<Grade> grades = gradeService.getAllGrades();
        return ResponseEntity.ok(new ApiResponse("Lista de grados obtenida", grades));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createGrade(@RequestBody Grade grade) {
        Grade createdGrade = gradeService.createGrade(grade);
        return ResponseEntity.ok(new ApiResponse("Grado creado exitosamente", createdGrade));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getGradeById(@PathVariable Long id) {
        Grade grade = gradeService.getGradeById(id);
        return ResponseEntity.ok(new ApiResponse("Grado encontrado", grade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateGrade(
            @PathVariable Long id,
            @RequestBody Grade grade) {
        Grade updatedGrade = gradeService.updateGrade(id, grade);
        return ResponseEntity.ok(new ApiResponse("Grado actualizado", updatedGrade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.ok(new ApiResponse("Grado eliminado"));
    }

    @GetMapping("/by-level/{levelId}")
    public ResponseEntity<ApiResponse> getGradesByAcademicLevel(@PathVariable Long levelId) {
        List<Grade> grades = gradeService.getGradesByAcademicLevel(levelId);
        return ResponseEntity.ok(new ApiResponse("Grados por nivel académico", grades));
    }
}
