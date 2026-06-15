package com.innervix.model3d.domain.game.model;

import java.time.LocalDateTime;

public record GameSession(
        String id,
        String playerName,
        String levelId,
        String stateJson,
        LocalDateTime startedAt,
        LocalDateTime lastSeenAt
) {
}
