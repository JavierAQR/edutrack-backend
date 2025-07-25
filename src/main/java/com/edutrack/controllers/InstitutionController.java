package com.edutrack.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.request.InstitutionDTO;
import com.edutrack.entities.Institution;
import com.edutrack.services.InstitutionService;

@RestController
@RequestMapping("/admin/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @GetMapping("/dto")
    public List<InstitutionDTO> getAllDTO() {
        return institutionService.getAllInstitutionsAsDTO();
    }

    @GetMapping("/{id}")
    public Institution getById(@PathVariable Long id) {
        return institutionService.getInstitutionById(id);
    }

    @PostMapping
    public ResponseEntity<Institution> create(@RequestBody Institution institution) {
        return new ResponseEntity<>(institutionService.createInstitution(institution), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Institution update(@PathVariable Long id, @RequestBody Institution institution) {
        return institutionService.updateInstitution(id, institution);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        institutionService.deleteInstitution(id);
        return ResponseEntity.noContent().build();
    }
}
