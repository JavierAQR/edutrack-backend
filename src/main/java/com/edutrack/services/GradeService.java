package com.edutrack.services;

import com.edutrack.entities.Grade;
import com.edutrack.repositories.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Grade createGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
    }

    public Grade updateGrade(Long id, Grade grade) {
        Grade existingGrade = getGradeById(id);
        existingGrade.setName(grade.getName());
        existingGrade.setAcademicLevel(grade.getAcademicLevel());
        return gradeRepository.save(existingGrade);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }

    public List<Grade> getGradesByAcademicLevel(Long levelId) {
        return gradeRepository.findByAcademicLevelId(levelId);
    }
}
