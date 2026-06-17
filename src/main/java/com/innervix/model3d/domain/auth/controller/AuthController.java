package com.innervix.model3d.domain.auth.controller;


import com.innervix.model3d.domain.auth.model.*;
import com.innervix.model3d.domain.auth.service.AuthService;
import com.innervix.model3d.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping({"/api/v1/admin/auth/login", "/api/admin/auth/login"})
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @GetMapping("/api/auth/csrf")
    public ApiResponse<Map<String, String>> csrf() {
        return ApiResponse.ok(Map.of("csrfToken", "java-csrf-disabled"));
    }

    @GetMapping("/api/admin/auth/me")
    public ApiResponse<Map<String, Object>> adminMe() {
        return ApiResponse.ok(Map.of(
                "admin", Map.of(
                        "id", "java-admin",
                        "email", "admin",
                        "role", "super_admin",
                        "permissions", List.of("*")
                )
        ));
    }
}
