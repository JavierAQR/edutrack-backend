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

import com.edutrack.dto.request.GradeDTO;
import com.edutrack.entities.Grade;
import com.edutrack.services.GradeService;

@RestController
@RequestMapping("/admin/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    public List<GradeDTO> getGradesWithLevelNames() {
        return gradeService.getAllWithLevelNames();
    }

    @GetMapping("/{id}")
    public Grade getGradeById(@PathVariable Long id) {
        return gradeService.getGradeById(id)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado con id " + id));
    }

    @PostMapping
    public Grade createGrade(@RequestBody GradeDTO dto) {
        return gradeService.createGrade(dto);
    }

    @PutMapping("/{id}")
    public Grade updateGrade(@PathVariable Long id, @RequestBody GradeDTO dto) {
        return gradeService.updateGrade(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
    }

    @GetMapping("/by-level/{levelId}")
    public List<Grade> getGradesByLevel(@PathVariable Long levelId) {
    return gradeService.getGradesByLevel(levelId);
}
}
