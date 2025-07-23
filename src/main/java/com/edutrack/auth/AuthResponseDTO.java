package com.edutrack.auth;

public record AuthResponseDTO(
    String token,
    AuthStatus authStatus,
    String message) {
    
}
