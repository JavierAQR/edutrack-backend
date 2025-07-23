package com.edutrack.controllers;

import com.edutrack.entities.AcademicPeriod;
import com.edutrack.services.AcademicPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periods")
public class AcademicPeriodController {

    @Autowired
    private AcademicPeriodService academicPeriodService;

    @GetMapping
    public List<AcademicPeriod> getAll() {
        return academicPeriodService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicPeriod> getById(@PathVariable Long id) {
        return academicPeriodService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    

    @PostMapping
    public AcademicPeriod create(@RequestBody AcademicPeriod academicPeriod) {
        return academicPeriodService.save(academicPeriod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicPeriod> update(@PathVariable Long id, @RequestBody AcademicPeriod academicPeriod) {
        return academicPeriodService.findById(id)
                .map(existing -> {
                    academicPeriod.setId(id);
                    return ResponseEntity.ok(academicPeriodService.save(academicPeriod));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (academicPeriodService.findById(id).isPresent()) {
            academicPeriodService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}