package com.parcial2.auth.dto;

import com.parcial2.auth.model.Role;

public record UserResponse(
    Long id,
    String name,
    String email,
    Role role
) {
}

