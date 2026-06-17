package com.innervix.model3d.domain.auth.model;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(String email, String username, @NotBlank String password) {
    public String loginName() {
        if (username != null && !username.isBlank()) {
            return username;
        }
        return email;
    }
}
