package com.edutrack.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edutrack.dto.request.CreateAssignmentRequest;
import com.edutrack.dto.response.AssignmentResponse;
import com.edutrack.entities.Assignment;
import com.edutrack.services.AssignmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") String type,
            @RequestParam("dueDate") String dueDate,
            @RequestParam("sectionId") Long sectionId,
            @RequestParam("teacherId") Long teacherId) throws IOException {

        CreateAssignmentRequest request = new CreateAssignmentRequest();
        request.setTitle(title);
        request.setDescription(description);
        request.setType(type);
        request.setDueDate(LocalDate.parse(dueDate));
        request.setSectionId(sectionId);

        Assignment assignment = assignmentService.createAssignment(request, file, teacherId);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<AssignmentResponse>> getBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsBySection(sectionId));
    }
}
