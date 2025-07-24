package com.edutrack.services;

import com.edutrack.dto.response.admin.AdminCourseDTO;
import com.edutrack.dto.request.admin.AdminCourseRequestDTO;
import com.edutrack.dto.response.admin.AdminGradeDTO;
import com.edutrack.entities.*;
import com.edutrack.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionAdminCourseService {

    private final CourseRepository courseRepository;
    private final GradeRepository gradeRepository;
    private final InstitutionGradeRepository institutionGradeRepository;
    private final InstitutionRepository institutionRepository;

    public List<AdminCourseDTO> getCoursesByInstitution(Long institutionId) {
        return courseRepository.findByInstitutionId(institutionId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AdminGradeDTO> getGradesByInstitution(Long institutionId) {
        return institutionGradeRepository.findByInstitutionId(institutionId)
                .stream()
                .map(this::convertGradeToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdminCourseDTO createCourse(Long institutionId, AdminCourseRequestDTO requestDTO) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        Grade grade = gradeRepository.findById(requestDTO.getGradeId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        // Verificar que el grado pertenezca a la institución
        institutionGradeRepository.findByInstitutionIdAndGradeId(institutionId, grade.getId())
                .orElseThrow(() -> new RuntimeException("El grado no pertenece a esta institución"));

        Course course = new Course();
        course.setName(requestDTO.getName());
        course.setGrade(grade);
        course = courseRepository.save(course);

        return convertToDTO(course);
    }

    @Transactional
    public AdminCourseDTO updateCourse(Long institutionId, Long courseId, AdminCourseRequestDTO requestDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        // Verificar que el curso pertenezca a la institución (a través del grado)
        institutionGradeRepository.findByInstitutionIdAndGradeId(institutionId, course.getGrade().getId())
                .orElseThrow(() -> new RuntimeException("El curso no pertenece a esta institución"));

        Grade grade = gradeRepository.findById(requestDTO.getGradeId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        // Verificar que el nuevo grado pertenezca a la institución
        institutionGradeRepository.findByInstitutionIdAndGradeId(institutionId, grade.getId())
                .orElseThrow(() -> new RuntimeException("El grado no pertenece a esta institución"));

        course.setName(requestDTO.getName());
        course.setGrade(grade);
        course = courseRepository.save(course);

        return convertToDTO(course);
    }

    @Transactional
    public void deleteCourse(Long institutionId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        // Verificar que el curso pertenezca a la institución (a través del grado)
        institutionGradeRepository.findByInstitutionIdAndGradeId(institutionId, course.getGrade().getId())
                .orElseThrow(() -> new RuntimeException("El curso no pertenece a esta institución"));

        courseRepository.delete(course);
    }

    private AdminCourseDTO convertToDTO(Course course) {
        AdminCourseDTO dto = new AdminCourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setGradeId(course.getGrade().getId());
        dto.setGradeName(course.getGrade().getName());
        dto.setAcademicLevelId(course.getGrade().getAcademicLevel().getId());
        dto.setAcademicLevelName(course.getGrade().getAcademicLevel().getName());
        return dto;
    }

    private AdminGradeDTO convertGradeToDTO(InstitutionGrade institutionGrade) {
        AdminGradeDTO dto = new AdminGradeDTO();
        dto.setId(institutionGrade.getGrade().getId());
        dto.setName(institutionGrade.getGrade().getName());
        dto.setAcademicLevelId(institutionGrade.getAcademicLevel().getId());
        dto.setAcademicLevelName(institutionGrade.getAcademicLevel().getName());
        return dto;
    }
}