package com.edutrack.services;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.edutrack.entities.PrecioInstitution;
import com.edutrack.repositories.PrecioInstitutionRepository;

@Service
@RequiredArgsConstructor
public class PrecioInstitutionService {

    private final PrecioInstitutionRepository repository;

    public List<PrecioInstitution> getByInstitution(Long institutionId) {
        return repository.findByInstitutionId(institutionId);
    }

    public PrecioInstitution create(PrecioInstitution precio) {
        return repository.save(precio);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
