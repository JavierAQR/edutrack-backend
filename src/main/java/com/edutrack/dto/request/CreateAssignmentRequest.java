package com.edutrack.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateAssignmentRequest {
    private String title;
    private String description;
    private String type; // "TAREA" o "MATERIAL"
    private LocalDate dueDate;
    private Long sectionId;
}