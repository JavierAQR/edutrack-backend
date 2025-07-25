package com.edutrack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.entities.AcademicLevel;
import com.edutrack.entities.InstitutionAcademicLevels;
import com.edutrack.services.InstitutionAcademicLevelsService;

@RestController
@RequestMapping("/admin/institution-academic-levels")
public class InstitutionAcademicLevelsController {
    @Autowired
    private InstitutionAcademicLevelsService service;

    @GetMapping
    public List<InstitutionAcademicLevels> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public InstitutionAcademicLevels getById(@PathVariable Long id) {
        return service.getById(id)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada"));
    }

    @PostMapping
    public InstitutionAcademicLevels create(
        @RequestParam Long institutionId,
        @RequestParam Long academicLevelId
    ) {
        return service.create(institutionId, academicLevelId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/by-institution/{institutionId}")
public List<AcademicLevel> getLevelsByInstitution(@PathVariable Long institutionId) {
    return service.getLevelsByInstitution(institutionId);
}
}
