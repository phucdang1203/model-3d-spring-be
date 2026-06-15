package com.innervix.model3d.domain.game.controller;


import com.innervix.model3d.domain.game.model.*;
import com.innervix.model3d.domain.game.service.GameSessionService;
import com.innervix.model3d.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
