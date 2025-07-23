package com.edutrack.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.edutrack.dto.response.AssignmentDTO;
import com.edutrack.dto.response.AssignmentGradeDTO;
import com.edutrack.dto.response.SectionResponse;
import com.edutrack.dto.response.SectionStudentDashboardResponse;
import com.edutrack.dto.response.StudentInSectionResponse;
import com.edutrack.dto.response.StudentWithAverageResponse;
import com.edutrack.entities.AcademicLevel;
import com.edutrack.entities.Assignment;
import com.edutrack.entities.AssignmentSubmission;
import com.edutrack.entities.Course;
import com.edutrack.entities.Grade;
import com.edutrack.entities.Institution;
import com.edutrack.entities.Section;
import com.edutrack.entities.StudentProfile;
import com.edutrack.entities.TeacherProfile;
import com.edutrack.entities.User;
import com.edutrack.repositories.AssignmentRepository;
import com.edutrack.repositories.AssignmentSubmissionRepository;
import com.edutrack.repositories.CourseRepository;
import com.edutrack.repositories.InstitutionRepository;
import com.edutrack.repositories.SectionRepository;
import com.edutrack.repositories.StudentProfileRepository;
import com.edutrack.repositories.TeacherProfileRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final InstitutionRepository institutionRepository;
    private final AssignmentSubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;

    public Section createSection(Long courseId, Long teacherId, Long institutionId, String name) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        TeacherProfile teacher = teacherProfileRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        Section section = new Section();
        section.setName(name);
        section.setCourse(course);
        section.setTeacher(teacher);
        section.setInstitution(institution);

        return sectionRepository.save(section);
    }

    public List<SectionResponse> getSectionsByInstitution(Long institutionId) {
        List<Section> sections = sectionRepository.findByInstitutionId(institutionId);

        return sections.stream().map(section -> {
            Course course = section.getCourse();
            Grade grade = course.getGrade();
            AcademicLevel level = grade.getAcademicLevel();

            return new SectionResponse(
                    section.getId(),
                    section.getName(),

                    course.getId(),
                    course.getName(),

                    section.getTeacher().getId(),
                    section.getTeacher().getUser().getName(),

                    grade.getId(),
                    grade.getName(),

                    level.getId(),
                    level.getName(),

                    section.getInstitution().getId(),
                    section.getInstitution().getName());
        }).collect(Collectors.toList());
    }

    @Transactional
    public Section assignStudentsToSection(Long sectionId, List<Long> studentIds) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Sección no encontrada"));

        Course course = section.getCourse();
        Grade sectionGrade = course.getGrade();
        AcademicLevel sectionLevel = sectionGrade.getAcademicLevel();
        Institution institution = section.getInstitution();

        List<StudentProfile> students = studentProfileRepository.findAllById(studentIds);

        for (StudentProfile student : students) {
            User user = student.getUser();

            if (!user.getInstitution().getId().equals(institution.getId())) {
                throw new RuntimeException("El estudiante " + user.getName() + user.getLastname()
                        + " no pertenece a la misma institución");
            }

            if (student.getGrade() == null ||
                    !student.getGrade().getId().equals(sectionGrade.getId()) ||
                    !student.getGrade().getAcademicLevel().getId().equals(sectionLevel.getId())) {

                throw new RuntimeException("El estudiante " + user.getName() + user.getLastname()
                        + " no pertenece al mismo grado o nivel académico");
            }
        }

        section.setStudents(new ArrayList<>(students));

        return sectionRepository.save(section);
    }

    public List<StudentInSectionResponse> getStudentsInSection(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Sección no encontrada"));

        List<StudentProfile> students = section.getStudents();

        return students.stream().map(student -> {
            User user = student.getUser();
            Grade grade = student.getGrade();
            String academicLevelName = grade.getAcademicLevel().getName();

            return new StudentInSectionResponse(
                    student.getId(),
                    user.getName(),
                    user.getLastname(),
                    user.getEmail(),
                    grade.getName(),
                    academicLevelName);
        }).collect(Collectors.toList());
    }

    public List<StudentWithAverageResponse> getStudentsWithAveragesInSection(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        List<StudentProfile> students = section.getStudents();

        return students.stream().map(student -> {
            Double average = submissionRepository.findAverageGradeByStudentInSection(student.getId(), sectionId);
            String fullName = student.getUser().getName() + " " + student.getUser().getLastname();

            return new StudentWithAverageResponse(
                    student.getId(),
                    fullName,
                    average != null ? average : 0.0);
        }).collect(Collectors.toList());
    }

    public List<SectionStudentDashboardResponse> getStudentSectionDashboard(Long studentId) {

        List<Section> sections = sectionRepository.findByStudents_Id(studentId);

        return sections.stream().map(section -> {
            // Promedio general
            Double average = submissionRepository.findAverageGradeByStudentInSection(studentId, section.getId());

            // Historial de entregas con nota
            List<AssignmentSubmission> submissions = submissionRepository.findByStudentIdAndSectionId(studentId,
                    section.getId());
            List<AssignmentGradeDTO> gradedAssignments = submissions.stream().map(s -> {
                return new AssignmentGradeDTO(
                        s.getAssignment().getId(),
                        s.getAssignment().getTitle(),
                        s.getAssignment().getFileUrl(),
                        s.getGrade(),
                        s.getSubmittedAt());
            }).collect(Collectors.toList());

            // Tareas pendientes
            List<Assignment> pending = assignmentRepository.findPendingAssignmentsForStudent(section.getId(),
                    studentId);
            List<AssignmentDTO> pendingAssignments = pending.stream().map(a -> {
                return new AssignmentDTO(
                        a.getId(),
                        a.getTitle(),
                        a.getFileUrl(),
                        a.getDueDate());
            }).collect(Collectors.toList());

            return new SectionStudentDashboardResponse(
                    section.getId(),
                    section.getName(),
                    section.getCourse().getName(),
                    section.getTeacher().getUser().getName() + " " + section.getTeacher().getUser().getLastname(),
                    average != null ? average : 0.0,
                    gradedAssignments,
                    pendingAssignments);
        }).collect(Collectors.toList());
    }

    public List<SectionResponse> getSectionsByTeacherId(Long teacherId) {
        List<Section> sections = sectionRepository.findByTeacherId(teacherId);
    
        return sections.stream()
            .map(this::mapToSectionResponse)
            .collect(Collectors.toList());
    }

    private SectionResponse mapToSectionResponse(Section section) {
        Course course = courseRepository.findById(section.getCourse().getId())
            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    
        Grade grade = course.getGrade(); 
        AcademicLevel academicLevel = grade.getAcademicLevel(); 
    
        TeacherProfile teacher = teacherProfileRepository.findById(section.getTeacher().getId())
            .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
        User teacherUser = teacher.getUser(); 
    
        Institution institution = institutionRepository.findById(section.getInstitution().getId())
            .orElseThrow(() -> new RuntimeException("Institución no encontrada"));
    
        return new SectionResponse(
            section.getId(),
            section.getName(),
    
            course.getId(),
            course.getName(),
    
            teacher.getId(),
            teacherUser.getName() + " " + teacherUser.getLastname(),
    
            grade.getId(),
            grade.getName(),
    
            academicLevel.getId(),
            academicLevel.getName(),
    
            institution.getId(),
            institution.getName()
        );
    }
}
