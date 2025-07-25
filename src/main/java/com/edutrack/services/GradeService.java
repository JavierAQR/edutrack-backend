package com.edutrack.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutrack.dto.request.GradeDTO;
import com.edutrack.entities.AcademicLevel;
import com.edutrack.entities.Grade;
import com.edutrack.repositories.AcademicLevelRepository;
import com.edutrack.repositories.GradeRepository;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AcademicLevelRepository academicLevelRepository;

    public List<GradeDTO> getAllWithLevelNames() {
        return gradeRepository.findAll().stream()
                .map(g -> new GradeDTO(
                        g.getId(),
                        g.getName(),
                        g.getAcademicLevel().getId(),
                        g.getAcademicLevel().getName()))
                .collect(Collectors.toList());
    }

    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }

    public Grade createGrade(GradeDTO dto) {
        AcademicLevel level = academicLevelRepository.findById(dto.getAcademicLevelId())
                .orElseThrow(() -> new RuntimeException("Nivel académico no encontrado"));

        Grade grade = new Grade();
        grade.setName(dto.getName());
        grade.setAcademicLevel(level);

        return gradeRepository.save(grade);
    }

    public Grade updateGrade(Long id, GradeDTO dto) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado con id " + id));

        AcademicLevel level = academicLevelRepository.findById(dto.getAcademicLevelId())
                .orElseThrow(() -> new RuntimeException("Nivel académico no encontrado"));

        grade.setName(dto.getName());
        grade.setAcademicLevel(level);

        return gradeRepository.save(grade);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }

    public List<Grade> getGradesByLevel(Long levelId) {
        return gradeRepository.findByAcademicLevelId(levelId);
    }
}
