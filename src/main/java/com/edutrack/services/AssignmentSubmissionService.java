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

import com.edutrack.dto.response.SubmissionResponse;
import com.edutrack.entities.Assignment;
import com.edutrack.entities.AssignmentSubmission;
import com.edutrack.entities.StudentProfile;
import com.edutrack.entities.User;
import com.edutrack.repositories.AssignmentRepository;
import com.edutrack.repositories.AssignmentSubmissionRepository;
import com.edutrack.repositories.StudentProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssignmentSubmissionService {

    private final AssignmentRepository assignmentRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final AssignmentSubmissionRepository submissionRepository;

    public AssignmentSubmission submitAssignment(
            Long assignmentId,
            Long studentId,
            MultipartFile file,
            String comment) throws IOException {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        StudentProfile student = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        // Verificar si ya envió
        if (submissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentId).isPresent()) {
            throw new RuntimeException("Ya enviaste esta tarea");
        }

        // Guardar archivo
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads", "submissions");
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileUrl("/uploads/submissions/" + fileName);
        submission.setComment(comment);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setGrade(null);

        return submissionRepository.save(submission);
    }

    public List<SubmissionResponse> getSubmissionsByAssignment(Long assignmentId) {
        List<AssignmentSubmission> submissions = submissionRepository.findByAssignmentId(assignmentId);

        return submissions.stream().map(submission -> {
            StudentProfile student = submission.getStudent();
            User user = student.getUser();

            return new SubmissionResponse(
                    submission.getId(),
                    student.getId(),
                    user.getName() + " " + user.getLastname(),
                    submission.getFileUrl(),
                    submission.getSubmittedAt(),
                    submission.getComment(),
                    submission.getGrade());
        }).collect(Collectors.toList());
    }

    public AssignmentSubmission gradeSubmission(Long submissionId, Double grade) {
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));

        submission.setGrade(grade);
        return submissionRepository.save(submission);
    }
}
