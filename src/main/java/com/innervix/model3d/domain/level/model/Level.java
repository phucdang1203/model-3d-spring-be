package com.innervix.model3d.domain.level.model;

import java.time.LocalDateTime;

public record Level(
        String id,
        String name,
        String slug,
        String description,
        String status,
        String mapModelUrl,
        String playerCharacterJson,
        String playerSpawnJson,
        String robotSpawnJson,
        String robotStory,
        String storyGraphJson,
        String zombieSpawnsJson,
        String mapCharactersJson,
        String placedObjectsJson,
        Integer maxPlayers,
        LocalDateTime publishedAt,
        LocalDateTime archivedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
