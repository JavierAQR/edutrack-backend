package com.edutrack.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherProfileResponse {
    private Long id;
    private String fullName;
}
