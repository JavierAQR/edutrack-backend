package com.edutrack.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutrack.entities.AcademicLevel;
import com.edutrack.entities.Grade;
import com.edutrack.entities.Institution;
import com.edutrack.entities.InstitutionAcademicLevels;
import com.edutrack.entities.InstitutionGrade;
import com.edutrack.repositories.AcademicLevelRepository;
import com.edutrack.repositories.GradeRepository;
import com.edutrack.repositories.InstitutionAcademicLevelsRepository;
import com.edutrack.repositories.InstitutionGradeRepository;
import com.edutrack.repositories.InstitutionRepository;

@Service
public class InstitutionAcademicLevelsService {
     @Autowired
    private InstitutionAcademicLevelsRepository ialRepo;

    @Autowired
    private InstitutionRepository institutionRepo;

    @Autowired
    private AcademicLevelRepository academicLevelRepo;

    @Autowired
    private GradeRepository gradeRepo;

    @Autowired
    private InstitutionGradeRepository institutionGradeRepo;

    public List<InstitutionAcademicLevels> getAll() {
        return ialRepo.findAll();
    }

    public Optional<InstitutionAcademicLevels> getById(Long id) {
        return ialRepo.findById(id);
    }

    public InstitutionAcademicLevels create(Long institutionId, Long levelId) {
        Institution institution = institutionRepo.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        AcademicLevel level = academicLevelRepo.findById(levelId)
                .orElseThrow(() -> new RuntimeException("Nivel académico no encontrado"));
        
        

        InstitutionAcademicLevels newRelation = new InstitutionAcademicLevels();
        newRelation.setInstitution(institution);
        newRelation.setAcademicLevel(level);
        InstitutionAcademicLevels savedRelation = ialRepo.save(newRelation);

        // 2. Buscar los grados asociados a ese nivel
        List<Grade> grades = gradeRepo.findByAcademicLevelId(levelId);

        // 3. Crear InstitutionGrade por cada grado
        List<InstitutionGrade> institutionGrades = grades.stream().map(grade -> {
            InstitutionGrade ig = new InstitutionGrade();
            ig.setInstitution(institution);
            ig.setAcademicLevel(level);
            ig.setGrade(grade);
            return ig;
        }).collect(Collectors.toList());

        institutionGradeRepo.saveAll(institutionGrades);

        return savedRelation;
    }

    public void delete(Long id) {
        ialRepo.deleteById(id);
    }

    public List<AcademicLevel> getLevelsByInstitution(Long institutionId) {
        return ialRepo.findByInstitutionId(institutionId).stream()
                .map(InstitutionAcademicLevels::getAcademicLevel)
                .collect(Collectors.toList());
    }
}
