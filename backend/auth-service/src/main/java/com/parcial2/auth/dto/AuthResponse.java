package com.parcial2.auth.dto;

public record AuthResponse(
    String token,
    UserResponse user
) {
}

