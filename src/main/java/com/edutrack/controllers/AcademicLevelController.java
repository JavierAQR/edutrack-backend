package com.edutrack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.entities.AcademicLevel;
import com.edutrack.services.AcademicLevelService;

@RestController
@RequestMapping("/admin/academic-levels")
public class AcademicLevelController {
    
    @Autowired
    private AcademicLevelService academicLevelService;

    @GetMapping
    public List<AcademicLevel> getAll() {
        return academicLevelService.getAll();
    }

    @GetMapping("/{id}")
    public AcademicLevel getById(@PathVariable Long id) {
        return academicLevelService.getById(id)
                .orElseThrow(() -> new RuntimeException("AcademicLevel no encontrado con ID: " + id));
    }

    @PostMapping
    public AcademicLevel create(@RequestBody AcademicLevel level) {
        return academicLevelService.create(level);
    }

    @PutMapping("/{id}")
    public AcademicLevel update(@PathVariable Long id, @RequestBody AcademicLevel level) {
        return academicLevelService.update(id, level);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        academicLevelService.delete(id);
    }
}
