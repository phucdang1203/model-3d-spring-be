package com.innervix.model3d.domain.game.model;

import jakarta.validation.constraints.NotBlank;

public record StartGameSessionRequest(@NotBlank String playerName, String levelId, String stateJson) {
}
