package com.edutrack.services;

import com.edutrack.entities.AcademicLevel;
import com.edutrack.repositories.AcademicLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicLevelService {

    private final AcademicLevelRepository academicLevelRepository;

    public List<AcademicLevel> getAllAcademicLevels() {
        return academicLevelRepository.findAll();
    }

    public AcademicLevel createAcademicLevel(AcademicLevel academicLevel) {
        return academicLevelRepository.save(academicLevel);
    }

    public AcademicLevel getAcademicLevelById(Long id) {
        return academicLevelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nivel académico no encontrado"));
    }

    public AcademicLevel updateAcademicLevel(Long id, AcademicLevel academicLevel) {
        AcademicLevel existingLevel = getAcademicLevelById(id);
        existingLevel.setName(academicLevel.getName());
        return academicLevelRepository.save(existingLevel);
    }

    public void deleteAcademicLevel(Long id) {
        academicLevelRepository.deleteById(id);
    }
}
