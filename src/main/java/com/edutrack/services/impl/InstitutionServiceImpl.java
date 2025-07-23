package com.edutrack.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.edutrack.dto.request.InstitutionDTO;
import com.edutrack.entities.Institution;
import com.edutrack.exception.ResourceNotFoundException;
import com.edutrack.repositories.InstitutionRepository;
import com.edutrack.services.InstitutionService;

@Service
public class InstitutionServiceImpl implements InstitutionService{
     private final InstitutionRepository institutionRepository;

    public InstitutionServiceImpl(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    @Override
    public List<InstitutionDTO> getAllInstitutionsAsDTO() {
    List<Institution> institutions = institutionRepository.findAll();

    return institutions.stream().map(inst -> {
        InstitutionDTO dto = new InstitutionDTO();
        dto.setId(inst.getId());
        dto.setName(inst.getName());
        dto.setAddress(inst.getAddress());
        dto.setDescription(inst.getDescription());
        dto.setPhone(inst.getPhone());
        dto.setWebsite(inst.getWebsite());

        List<String> levels = inst.getAcademicLevels().stream()
            .map(rel -> rel.getAcademicLevel().getName())
            .toList();

        dto.setAcademicLevels(levels);
        return dto;
    }).toList();
}


    @Override
    public Institution getInstitutionById(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Institution not found with id: " + id));
    }

    @Override
    public Institution createInstitution(Institution institution) {
        return institutionRepository.save(institution);
    }

    @Override
    public Institution updateInstitution(Long id, Institution updatedInstitution) {
        Institution existing = getInstitutionById(id);
        existing.setName(updatedInstitution.getName());
        existing.setAddress(updatedInstitution.getAddress());
        existing.setDescription(updatedInstitution.getDescription());
        existing.setPhone(updatedInstitution.getPhone());
        existing.setWebsite(updatedInstitution.getWebsite());
        return institutionRepository.save(existing);
    }

    @Override
    public void deleteInstitution(Long id) {
        Institution existing = getInstitutionById(id);
        institutionRepository.delete(existing);
    }


}
