package com.innervix.model3d.domain.auth.service;


import com.innervix.model3d.domain.auth.mapper.AdminMapper;
import com.innervix.model3d.domain.auth.model.*;
import com.innervix.model3d.security.jwt.JwtService;
import com.innervix.model3d.config.AppProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements CommandLineRunner {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AppProperties properties;

    public AuthService(AdminMapper adminMapper, PasswordEncoder passwordEncoder, JwtService jwtService, AppProperties properties) {
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.properties = properties;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        AdminUser admin = adminMapper.findByUsername(request.loginName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        if (!admin.enabled() || !passwordEncoder.matches(request.password(), admin.passwordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return new LoginResponse(jwtService.createAdminToken(admin.username()), admin.username(), admin.role());
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!adminMapper.existsAny()) {
            var bootstrap = properties.getSecurity().getBootstrapAdmin();
            int created = adminMapper.create(
                    bootstrap.getUsername(),
                    passwordEncoder.encode(bootstrap.getPassword()),
                    "Bootstrap Admin"
            );
            if (created != 1) {
                throw new IllegalStateException("Bootstrap admin was not created");
            }
        }
    }
}
