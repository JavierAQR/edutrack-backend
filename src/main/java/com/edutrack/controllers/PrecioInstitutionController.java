
package com.edutrack.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.entities.PrecioInstitution;
import com.edutrack.services.PrecioInstitutionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/precios")
@RequiredArgsConstructor
public class PrecioInstitutionController {

    private final PrecioInstitutionService service;

    @GetMapping("/institution/{institutionId}")
    public List<PrecioInstitution> getByInstitution(@PathVariable Long institutionId) {
        return service.getByInstitution(institutionId);
    }

    @PostMapping
    public PrecioInstitution create(@RequestBody PrecioInstitution precio) {
        return service.create(precio);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
