package com.innervix.model3d.domain.level.controller;


import com.innervix.model3d.domain.level.model.*;
import com.innervix.model3d.domain.level.service.LevelService;
import com.innervix.model3d.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LevelController {

    private final LevelService service;

    public LevelController(LevelService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/game/levels")
    public ApiResponse<List<Level>> listForGame() {
        return ApiResponse.ok(service.list());
    }

    @GetMapping("/api/v1/admin/levels")
    public ApiResponse<List<Level>> listForAdmin() {
        return ApiResponse.ok(service.list());
    }

    @PostMapping("/api/v1/admin/levels")
    public ApiResponse<Level> save(@Valid @RequestBody LevelRequest request) {
        return ApiResponse.ok(service.save(request));
    }

    @DeleteMapping("/api/v1/admin/levels/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ApiResponse.ok();
    }
}
