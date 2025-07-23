package com.edutrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private Object data;
    
    // Constructor adicional solo con mensaje
    public ApiResponse(String message) {
        this.message = message;
        this.data = null;
    }
}