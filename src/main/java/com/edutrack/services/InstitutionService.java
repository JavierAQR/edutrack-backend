package com.edutrack.services;

import java.util.List;

import com.edutrack.dto.request.InstitutionDTO;
import com.edutrack.entities.AcademicLevel;
import com.edutrack.entities.Institution;

public interface InstitutionService {
    List<InstitutionDTO> getAllInstitutionsAsDTO();
    Institution getInstitutionById(Long id);
    Institution createInstitution(Institution institution);
    Institution updateInstitution(Long id, Institution institution);
    void deleteInstitution(Long id);
    List<AcademicLevel> getAcademicLevelsByInstitution(Long institutionId);
}
