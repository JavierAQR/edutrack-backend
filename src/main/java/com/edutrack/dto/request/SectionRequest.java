package com.edutrack.dto.request;

import lombok.Data;

@Data
public class SectionRequest {
    private Long courseId;
    private Long teacherId;
    private Long institutionId;
    private String name;
}