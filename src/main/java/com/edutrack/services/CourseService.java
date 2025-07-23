package com.edutrack.services;

import com.edutrack.dto.request.CourseDTO;
import com.edutrack.entities.AcademicLevel;
import com.edutrack.entities.Course;
import com.edutrack.entities.Grade;
import com.edutrack.entities.InstitutionAcademicLevels;
import com.edutrack.repositories.CourseRepository;
import com.edutrack.repositories.GradeRepository;
import com.edutrack.repositories.InstitutionAcademicLevelsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

     @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private InstitutionAcademicLevelsRepository institutionAcademicLevelRepository;

    public List<CourseDTO> getAll() {
        return courseRepository.findAll().stream()
                .map(course -> {
                    Grade grade = course.getGrade();
                    AcademicLevel level = grade.getAcademicLevel();
    
                    // Buscar la institución a través de la tabla intermedia
                    InstitutionAcademicLevels ial = institutionAcademicLevelRepository
                            .findByAcademicLevelId(level.getId())
                            .orElse(null);
    
                    return new CourseDTO(
                            course.getId(),
                            course.getName(),
                            grade.getId(),
                            grade.getName(),
                            level.getId(),
                            level.getName(),
                            ial != null ? ial.getInstitution().getId() : null,
                            ial != null ? ial.getInstitution().getName() : null
                    );
                })
                .collect(Collectors.toList());
    }

    public Course getById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    }

    public Course create(CourseDTO dto) {
        if (courseRepository.existsByNameAndGradeId(dto.getName(), dto.getGradeId())) {
            throw new RuntimeException("Ya existe un curso con ese nombre en ese grado");
        }
        Grade grade = gradeRepository.findById(dto.getGradeId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
        Course course = new Course(null, dto.getName(), grade);
        return courseRepository.save(course);
    }

    public Course update(Long id, CourseDTO dto) {
        Course existing = getById(id);
        Grade grade = gradeRepository.findById(dto.getGradeId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
        existing.setName(dto.getName());
        existing.setGrade(grade);
        return courseRepository.save(existing);
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
    
}
