package com.innervix.model3d.domain.level.model;

import java.time.LocalDateTime;

public record Level(
        String id,
        String name,
        String mapModelUrl,
        String playerCharacterJson,
        String playerSpawnJson,
        String robotSpawnJson,
        String robotStory,
        String storyGraphJson,
        String zombieSpawnsJson,
        String placedObjectsJson,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
