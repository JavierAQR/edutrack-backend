package com.edutrack.dto.request.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminCourseRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    private Long gradeId;
}