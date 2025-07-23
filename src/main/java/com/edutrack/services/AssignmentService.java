package com.edutrack.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.edutrack.dto.request.CreateAssignmentRequest;
import com.edutrack.dto.response.AssignmentResponse;
import com.edutrack.entities.Assignment;
import com.edutrack.entities.Section;
import com.edutrack.entities.TeacherProfile;
import com.edutrack.repositories.AssignmentRepository;
import com.edutrack.repositories.SectionRepository;
import com.edutrack.repositories.TeacherProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SectionRepository sectionRepository;
    private final TeacherProfileRepository teacherProfileRepository;

    public Assignment createAssignment(CreateAssignmentRequest request, MultipartFile file, Long teacherId)
            throws IOException {
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new RuntimeException("Sección no encontrada"));

        TeacherProfile teacher = teacherProfileRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado"));

        if (!section.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("No tienes permiso para subir tareas a esta sección");
        }

        // Guardar archivo en disco local o sistema cloud (simple: local)
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads/assignments");
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Assignment assignment = new Assignment();
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setType(request.getType());
        assignment.setDueDate(request.getDueDate());
        assignment.setSection(section);
        assignment.setTeacher(teacher);
        assignment.setFileUrl("/uploads/assignments/" + fileName);
        assignment.setCreatedAt(LocalDateTime.now());

        return assignmentRepository.save(assignment);
    }

    public List<AssignmentResponse> getAssignmentsBySection(Long sectionId) {
        List<Assignment> assignments = assignmentRepository.findBySectionId(sectionId);

        return assignments.stream()
                .map(a -> new AssignmentResponse(
                        a.getId(),
                        a.getTitle(),
                        a.getDescription(),
                        a.getType(),
                        a.getDueDate(),
                        a.getFileUrl()))
                .collect(Collectors.toList());
    }
}