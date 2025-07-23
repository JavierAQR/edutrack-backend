package com.edutrack.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.dto.request.InstitutionGradeDTO;
import com.edutrack.services.InstitutionGradeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/institution-grades")
@RequiredArgsConstructor
public class InstitutionGradeController {
    private final InstitutionGradeService service;

    @GetMapping
    public List<InstitutionGradeDTO> getAll() {
        return service.findAll().stream()
                .map(service::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public InstitutionGradeDTO getById(@PathVariable Long id) {
        return service.toDTO(service.findById(id));
    }

    @PostMapping
    public InstitutionGradeDTO create(@RequestBody InstitutionGradeDTO dto) {
        return service.toDTO(service.save(service.fromDTO(dto)));
    }

    @PutMapping("/{id}")
    public InstitutionGradeDTO update(@PathVariable Long id, @RequestBody InstitutionGradeDTO dto) {
        return service.toDTO(service.update(id, service.fromDTO(dto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
