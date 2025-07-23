package com.edutrack.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.edutrack.dto.request.InstitutionGradeDTO;
import com.edutrack.entities.AcademicLevel;
import com.edutrack.entities.Grade;
import com.edutrack.entities.Institution;
import com.edutrack.entities.InstitutionGrade;
import com.edutrack.repositories.InstitutionGradeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstitutionGradeService {
    private final InstitutionGradeRepository repository;

    public List<InstitutionGrade> findAll() {
        return repository.findAll();
    }

    public InstitutionGrade findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("InstitutionGrade no encontrada"));
    }

    public InstitutionGrade save(InstitutionGrade institutionGrade) {
        return repository.save(institutionGrade);
    }

    public InstitutionGrade update(Long id, InstitutionGrade updated) {
        InstitutionGrade existing = findById(id);
        existing.setInstitution(updated.getInstitution());
        existing.setAcademicLevel(updated.getAcademicLevel());
        existing.setGrade(updated.getGrade());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public InstitutionGradeDTO toDTO(InstitutionGrade entity) {
        return new InstitutionGradeDTO(
                entity.getId(),
                entity.getInstitution().getId(),
                entity.getInstitution().getName(),
                entity.getAcademicLevel().getId(),
                entity.getAcademicLevel().getName(),
                entity.getGrade().getId(),
                entity.getGrade().getName());
    }

    public InstitutionGrade fromDTO(InstitutionGradeDTO dto) {
        Institution institution = new Institution();
        institution.setId(dto.getInstitutionId());

        AcademicLevel level = new AcademicLevel();
        level.setId(dto.getAcademicLevelId());

        Grade grade = new Grade();
        grade.setId(dto.getGradeId());

        return new InstitutionGrade(dto.getId(), institution, level, grade);
    }
}
