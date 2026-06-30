package com.innervix.model3d.domain.game.controller;


import com.innervix.model3d.domain.game.model.*;
import com.innervix.model3d.domain.game.service.GameSessionService;
import com.innervix.model3d.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
public class GameController {

    private final GameSessionService service;

    public GameController(GameSessionService service) {
        this.service = service;
    }

    @PostMapping("/api/v1/game/sessions/start")
    public ApiResponse<GameSession> start(@Valid @RequestBody StartGameSessionRequest request) {
        return ApiResponse.ok(service.start(request));
    }

    @PostMapping({"/api/maps/{mapId}/join-intent", "/api/v1/game/maps/{mapId}/join-intent"})
    public ApiResponse<Void> joinIntent(@PathVariable String mapId) {
        return ApiResponse.failed("Realtime room is disabled for the Java API fallback.");
    }

    @GetMapping({"/api/maps/{mapId}/presence", "/api/v1/game/maps/{mapId}/presence"})
    public ApiResponse<Map<String, Object>> presence(@PathVariable String mapId) {
        return ApiResponse.ok(Map.of(
                "mapId", mapId,
                "players", List.of(),
                "serverTimeMs", Instant.now().toEpochMilli()
        ));
    }

    @PostMapping({"/api/maps/{mapId}/presence", "/api/v1/game/maps/{mapId}/presence"})
    public ApiResponse<Map<String, Object>> heartbeat(
            @PathVariable String mapId,
            @RequestBody(required = false) Map<String, Object> body
    ) {
        return presence(mapId);
    }

    @GetMapping({"/api/maps/{mapId}/chat/history", "/api/v1/game/maps/{mapId}/chat/history"})
    public ApiResponse<Map<String, Object>> chatHistory(
            @PathVariable String mapId,
            @RequestParam(defaultValue = "60") int limit
    ) {
        return ApiResponse.ok(Map.of(
                "mapId", mapId,
                "messages", List.of(),
                "limit", Math.max(0, Math.min(limit, 200))
        ));
    }

    @PostMapping({"/api/maps/{mapId}/chat/messages", "/api/v1/game/maps/{mapId}/chat/messages"})
    public ApiResponse<Map<String, Object>> sendChatMessage(
            @PathVariable String mapId,
            @RequestBody(required = false) Map<String, Object> body
    ) {
        return ApiResponse.ok(Map.of(
                "mapId", mapId,
                "message", Map.of(
                        "id", "local-" + Instant.now().toEpochMilli(),
                        "serverTimeMs", Instant.now().toEpochMilli()
                )
        ));
    }
}
