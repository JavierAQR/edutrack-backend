package com.edutrack.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class AssignStudentsRequest {
    private List<Long> studentIds;
}
