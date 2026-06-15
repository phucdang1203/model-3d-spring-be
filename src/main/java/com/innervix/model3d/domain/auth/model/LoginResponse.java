package com.innervix.model3d.domain.auth.model;

public record LoginResponse(String accessToken, String username, String role) {
}
