package com.innervix.model3d.domain.auth.model;

public record AdminUser(Long id, String username, String passwordHash, String displayName, String role, Boolean enabled) {
}
