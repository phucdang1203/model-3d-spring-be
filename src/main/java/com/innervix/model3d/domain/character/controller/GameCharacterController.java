package com.innervix.model3d.domain.character.controller;

import com.innervix.model3d.common.ApiResponse;
import com.innervix.model3d.domain.character.model.*;
import com.innervix.model3d.domain.character.service.GameCharacterService;
import com.innervix.model3d.domain.level.model.LevelResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GameCharacterController {

    private final GameCharacterService service;

    public GameCharacterController(GameCharacterService service) {
        this.service = service;
    }

    @GetMapping({"/api/admin/characters", "/api/v1/admin/characters"})
    public ApiResponse<List<GameCharacterResponse>> list(
            @RequestParam(name = "includeInactive", defaultValue = "false") boolean includeInactive
    ) {
        return ApiResponse.ok(service.list(includeInactive));
    }

    @PostMapping({"/api/admin/characters", "/api/v1/admin/characters"})
    public ApiResponse<GameCharacterResponse> save(@Valid @RequestBody GameCharacterRequest request) {
        return ApiResponse.ok(service.save(request));
    }

    @DeleteMapping({"/api/admin/characters/{id}", "/api/v1/admin/characters/{id}"})
    public ApiResponse<GameCharacterResponse> archive(@PathVariable String id) {
        return ApiResponse.ok(service.archive(id));
    }

    @GetMapping({"/api/admin/maps/{id}/characters", "/api/v1/admin/maps/{id}/characters"})
    public ApiResponse<List<Map<String, Object>>> listMapCharacters(@PathVariable String id) {
        return ApiResponse.ok(service.listMapCharacters(id));
    }

    @PostMapping({"/api/admin/maps/{id}/characters", "/api/v1/admin/maps/{id}/characters"})
    public ApiResponse<LevelResponse> assignMapCharacter(
            @PathVariable String id,
            @Valid @RequestBody AssignMapCharacterRequest request
    ) {
        return ApiResponse.ok(service.assignToLevel(id, request));
    }
}
