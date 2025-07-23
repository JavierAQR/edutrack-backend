package com.edutrack.controllers;

import com.edutrack.entities.InstitutionGrade;
import com.edutrack.services.InstitutionGradeAdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/institution-admin/institution-grades")
public class InstitutionGradeAdminController {

    private final InstitutionGradeAdminService institutionGradeAdminService;

    public InstitutionGradeAdminController(InstitutionGradeAdminService service) {
        this.institutionGradeAdminService = service;
    }

    @GetMapping
    public List<InstitutionGrade> getAll() {
        return institutionGradeAdminService.getGradesForCurrentInstitutionAdmin();
    }

    @PostMapping
    public InstitutionGrade create(@RequestBody InstitutionGrade grade) {
        return institutionGradeAdminService.assignGrade(grade);
    }

    @PutMapping("/{id}")
    public InstitutionGrade update(@PathVariable Long id, @RequestBody InstitutionGrade grade) {
        return institutionGradeAdminService.updateGrade(id, grade);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        institutionGradeAdminService.deleteGrade(id);
    }
}
