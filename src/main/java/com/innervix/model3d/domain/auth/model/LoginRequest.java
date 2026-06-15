package com.innervix.model3d.domain.auth.model;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String username, @NotBlank String password) {
}
