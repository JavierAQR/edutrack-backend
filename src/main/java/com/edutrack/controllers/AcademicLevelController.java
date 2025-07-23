package com.edutrack.controllers;

import com.edutrack.dto.ApiResponse;
import com.edutrack.entities.AcademicLevel;
import com.edutrack.services.AcademicLevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic-levels")
public class AcademicLevelController {

    private final AcademicLevelService academicLevelService;

    public AcademicLevelController(AcademicLevelService academicLevelService) {
        this.academicLevelService = academicLevelService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAcademicLevels() {
        List<AcademicLevel> levels = academicLevelService.getAllAcademicLevels();
        return ResponseEntity.ok(new ApiResponse("Lista de niveles académicos obtenida", levels));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAcademicLevel(@RequestBody AcademicLevel academicLevel) {
        AcademicLevel createdLevel = academicLevelService.createAcademicLevel(academicLevel);
        return ResponseEntity.ok(new ApiResponse("Nivel académico creado exitosamente", createdLevel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAcademicLevelById(@PathVariable Long id) {
        AcademicLevel level = academicLevelService.getAcademicLevelById(id);
        return ResponseEntity.ok(new ApiResponse("Nivel académico encontrado", level));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAcademicLevel(
            @PathVariable Long id,
            @RequestBody AcademicLevel academicLevel) {
        AcademicLevel updatedLevel = academicLevelService.updateAcademicLevel(id, academicLevel);
        return ResponseEntity.ok(new ApiResponse("Nivel académico actualizado", updatedLevel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAcademicLevel(@PathVariable Long id) {
        academicLevelService.deleteAcademicLevel(id);
        return ResponseEntity.ok(new ApiResponse("Nivel académico eliminado"));
    }
}
